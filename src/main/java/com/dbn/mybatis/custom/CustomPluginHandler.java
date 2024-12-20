package com.dbn.mybatis.custom;

import com.dbn.compile.MyClassLoader;
import com.dbn.compile.MyJavaCompiler;
import com.dbn.driver.DatabaseDriverManager;
import com.dbn.mybatis.ui.CustomPluginEditorDialog;
import com.dbn.utils.NotifyUtil;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yudong
 */
public class CustomPluginHandler {
    private final Project project;
    public static final String PLUGIN_SIMPLE_NAME = "CustomPlugin";
    public static final String PLUGIN_CLASS_NAME = "com.dbn.mybatis.plugins." + PLUGIN_SIMPLE_NAME;

    public CustomPluginHandler(Project project) {
        this.project = project;
    }

    @SneakyThrows
    public void show() {
        if (DumbService.getInstance(project).isDumb()) {
            NotifyUtil.INSTANCE.notifyInfo(project,"IDEA is indexing, so feature no available!");
            return;
        }

        CustomPluginEditorDialog dialog = new CustomPluginEditorDialog(project);
        dialog.show();
    }

    @SneakyThrows
    public MyClassLoader compile(String javaCode) {
        MyClassLoader classLoader = createClassLoader();

        MyJavaCompiler.INSTANCE.compile(PLUGIN_CLASS_NAME, javaCode, classLoader, "-proc:none", "-encoding",
                "utf-8", "-cp", getJavacCommandLibrary());

        return classLoader;
    }


    /**
     * 获取脚本编译时候的-cp命令参数
     */
    private String getJavacCommandLibrary() {
        List<String> projectLibrary = new ArrayList<>();

        //必须依赖的
        projectLibrary.add(PathManager.getJarPathForClass(this.getClass()));

        projectLibrary.addAll(getUserProjectIncludeLibrary(project));

        //用户项目自己的输出路径
        projectLibrary.addAll(getClassOutputPaths(project));

        return String.join(File.pathSeparator, projectLibrary);
    }

    /**
     * 创建Classloader
     */
    private MyClassLoader createClassLoader() {
        List<URL> userLibrary = getUserProjectIncludeLibrary(project)
                .stream()
                .map(this::fileToURL)
                .toList();

        List<URL> classOutputPaths = getClassOutputPaths(project)
                .stream()
                .map(this::fileToURL)
                .toList();

        List<URL> paths = new ArrayList<>();
        paths.addAll(classOutputPaths);
        paths.addAll(userLibrary);

        return new MyClassLoader(paths.toArray(URL[]::new), DatabaseDriverManager.getClassLoader());
    }

    public static List<String> getClassOutputPaths(Project project) {
        List<String> result = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            try {
                @SuppressWarnings("DataFlowIssue")
                String path = CompilerModuleExtension.getInstance(module).getCompilerOutputPath().getPath();
                result.add(path);
                //这里有的模块获取不到，直接忽略
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    @SneakyThrows
    private URL fileToURL(String str) {
        return new File(str).toURI().toURL();
    }

    /**
     * 获取用户项目通过maven、gradle引入的第三方jar包
     */
    public static List<String> getUserProjectIncludeLibrary(Project project) {
        List<String> libraryNames = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            ModuleRootManager.getInstance(module).orderEntries()
                    .forEachLibrary(library -> {
                        String[] urls = library.getUrls(OrderRootType.CLASSES);
                        if (urls.length > 0) {
                            libraryNames.add(library.getUrls(OrderRootType.CLASSES)[0]);
                        }
                        return true;
                    });
        }

        return libraryNames.stream()
                .map(jarUrl -> {
                    String jarFile = jarUrl;
                    //上面获取的是jar://x.jar!/格式的数据，这里需要去除掉
                    if (jarFile.startsWith("jar://") && jarFile.endsWith("!/")) {
                        jarFile = jarFile.substring(6);
                        jarFile = jarFile.substring(0, jarFile.length() - 2);
                    }
                    return jarFile;
                })
                .collect(Collectors.toList());
    }

}
