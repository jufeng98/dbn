package com.dbn.mybatis.ui;

import com.dbn.DatabaseNavigator;
import com.dbn.common.thread.Progress;
import com.dbn.compile.MyClassLoader;
import com.dbn.mybatis.custom.CustomPluginHandler;
import com.dbn.mybatis.custom.JavaEditorTextField;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.DependencyScope;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.io.URLUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yudong
 */
public class CustomPluginEditorDialog extends DialogWrapper {
    private static final String SCRIPT_NAME = "mybatis-generator-core-1.4.2.jar";

    private final JavaEditorTextField textField;
    private final Project project;

    public CustomPluginEditorDialog(Project project) {
        super(project);
        this.project = project;

        addDependency(project, loadGeneratorJar());

        setOKActionEnabled(false);

        String javaCode = getCustomPluginCode(project);
        if (javaCode == null) {
            javaCode = getScriptTemplateCode();
        }

        textField = new JavaEditorTextField(project, javaCode);

        setSize(1000, 700);

        init();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        saveCustomPluginCode();
    }

    private void saveCustomPluginCode() {
        String javaCode = textField.getText();

        String path = project.getBasePath() + "/.idea";
        File file = new File(path, CustomPluginHandler.PLUGIN_SIMPLE_NAME + ".java");

        try {
            FileUtils.write(file, javaCode, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public static String getCustomPluginCode(Project project) {
        String path = project.getBasePath() + "/.idea";
        File file = new File(path, CustomPluginHandler.PLUGIN_SIMPLE_NAME + ".java");
        if (!file.exists()) {
            return null;
        }

        return FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
    }

    @Override
    protected Action @NotNull [] createActions() {
        List<Action> actions = List.of(new CompileTestAction(), getOKAction(), getCancelAction());
        return actions.toArray(Action[]::new);
    }

    private class CompileTestAction extends AbstractAction {

        private CompileTestAction() {
            super("Compile Test");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String javaCode = textField.getText();

            Progress.modal(project, null, false, "Tip", "Compiling...",
                    progress -> {
                        try {
                            CustomPluginHandler customPluginHandler = new CustomPluginHandler(project);
                            MyClassLoader myClassLoader = customPluginHandler.compile(javaCode);
                            myClassLoader.close();
                        } catch (Exception exception) {
                            progress.stop();

                            ApplicationManager.getApplication().invokeAndWait(() -> {
                                setOKActionEnabled(false);
                                Messages.showErrorDialog("Compile failed, reason:" + exception.getMessage(), "Tip");
                            });

                            return;
                        }

                        progress.stop();

                        ApplicationManager.getApplication().invokeAndWait(() -> {
                            setOKActionEnabled(true);
                            Messages.showInfoMessage("Compile success!", "Tip");
                        });
                    });
        }
    }

    private String loadGeneratorJar() {
        String libraryRoot = "/lib/ext/mybatis";

        @SuppressWarnings("DataFlowIssue")
        String absolutePath = PluginManagerCore.getPlugin(DatabaseNavigator.DBN_PLUGIN_ID)
                .getPluginPath().toFile().getAbsolutePath();

        return absolutePath + libraryRoot + "/" + SCRIPT_NAME;
    }

    public void addDependency(Project project, String jarPath) {
        fixScriptLibPath(project, jarPath);

        addScriptLibToProject(project, jarPath);

        addScriptLibToMainModule(project);
    }

    /**
     * 修正脚本lib路径，可能被用户篡改
     */
    private void fixScriptLibPath(Project project, String path) {
        LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        LibraryTable.ModifiableModel projectLibraryModel = projectLibraryTable.getModifiableModel();
        Library scriptLib = projectLibraryTable.getLibraryByName(SCRIPT_NAME);
        if (scriptLib == null) {
            return;
        }

        Library.ModifiableModel modifiableModel = scriptLib.getModifiableModel();
        for (String url : modifiableModel.getUrls(OrderRootType.CLASSES)) {
            modifiableModel.removeRoot(url, OrderRootType.CLASSES);
        }

        modifiableModel.addRoot(createScriptLibVirtualFile(path), OrderRootType.CLASSES);
        ApplicationManager.getApplication().invokeAndWait(() ->
                ApplicationManager.getApplication().runWriteAction(() -> {
                    modifiableModel.commit();
                    projectLibraryModel.commit();
                }));
    }

    private void addScriptLibToProject(Project project, String jarPath) {
        LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        final LibraryTable.ModifiableModel projectLibraryModel = projectLibraryTable.getModifiableModel();

        Library scriptLib = projectLibraryTable.getLibraryByName(SCRIPT_NAME);
        if (scriptLib != null) {
            return;
        }

        scriptLib = projectLibraryModel.createLibrary(SCRIPT_NAME);
        Library.ModifiableModel libraryModel = scriptLib.getModifiableModel();

        VirtualFile scriptLibVirtualFile = createScriptLibVirtualFile(jarPath);
        libraryModel.addRoot(scriptLibVirtualFile, OrderRootType.CLASSES);

        ApplicationManager.getApplication().runWriteAction(() -> {
            libraryModel.commit();
            projectLibraryModel.commit();
        });
    }

    private void addScriptLibToMainModule(Project project) {
        com.intellij.openapi.module.Module[] modules = ModuleManager.getInstance(project).getModules();
        com.intellij.openapi.module.Module mainModule = findMainModule(project);
        if (mainModule == null && modules.length > 0) {
            mainModule = modules[modules.length - 1];
        }

        if (mainModule == null) {
            return;
        }

        if (isExistInModule(mainModule)) {
            return;
        }

        LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
        Library scriptLib = projectLibraryTable.getLibraryByName(SCRIPT_NAME);
        if (scriptLib == null) {
            return;
        }

        Module finalMainModule = mainModule;
        ApplicationManager.getApplication().invokeAndWait(() ->
                ModuleRootModificationUtil.addDependency(finalMainModule, scriptLib, DependencyScope.COMPILE, false));

    }

    private com.intellij.openapi.module.Module findMainModule(Project project) {
        com.intellij.openapi.module.Module result = null;
        int moduleNameLength = Integer.MAX_VALUE;
        for (com.intellij.openapi.module.Module module : ModuleManager.getInstance(project).getModules()) {
            if (module.getName().length() < moduleNameLength) {
                moduleNameLength = module.getName().length();
                result = module;
            }
        }
        return result;
    }

    private boolean isExistInModule(Module module) {
        OrderEntry[] orderEntries = ModuleRootManager.getInstance(module).getOrderEntries();
        for (OrderEntry orderEntry : orderEntries) {
            if (orderEntry instanceof LibraryOrderEntry) {
                String libraryName = ((LibraryOrderEntry) orderEntry).getLibraryName();
                if (SCRIPT_NAME.equals(libraryName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private VirtualFile createScriptLibVirtualFile(String jarPath) {
        String pathUrl = VirtualFileManager.constructUrl(URLUtil.JAR_PROTOCOL, jarPath + JarFileSystem.JAR_SEPARATOR);
        return VirtualFileManager.getInstance().findFileByUrl(pathUrl);
    }

    @SneakyThrows
    private String getScriptTemplateCode() {
        ClassLoader classLoader = CustomPluginHandler.class.getClassLoader();
        URL url = classLoader.getResource("mybatis/" + CustomPluginHandler.PLUGIN_SIMPLE_NAME + ".java");
        @Cleanup
        @SuppressWarnings("DataFlowIssue")
        InputStream inputStream = url.openStream();
        return new String(StreamUtil.readBytes(inputStream), StandardCharsets.UTF_8);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return new JBScrollPane(textField);
    }

}
