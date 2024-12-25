package com.dbn.generator.ui;

import com.dbn.generate.GenerateHelper;
import com.google.gson.JsonObject;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class GenerateForm extends DialogWrapper {
    private final GenerateHelper generateHelper;
    private JBList<String> historyList;
    private JButton deleteHistoryBtn;

    private TextFieldWithBrowseButton projectFolderBtn;
    private TextFieldWithBrowseButton apiFolderBtn;
    private JBTextField pathTextField;
    private JBTextField nameTextField;
    private JBTextField businessNameTextField;
    private JBTextField methodNameTextField;
    private JBTextField controllerPostfixTextField;
    private JBTextField controllerPackageTextField;
    private JBTextField servicePostfixTextField;
    private JBTextField servicePackageTextField;
    private JBTextField pojoPostfixTextField;
    private JBTextField pojoPackageTextField;
    private JBTextField apiPathTextField;

    private JButton chooseControllerPackageBtn;
    private JButton chooseServicePackageBtn;
    private JButton choosePojoPackageBtn;

    private JPanel mainPanel;
    private JPanel apiPanel;
    private JBRadioButton feignRadioButton;
    private JBRadioButton dubboRadioButton;

    private final Project project;
    private String historySelectValue;

    public GenerateForm(Project project) {
        super(true);
        this.project = project;

        generateHelper = new GenerateHelper(project);

        initForm();

        loadConfig();

        init();
    }

    public void initForm() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(feignRadioButton);
        buttonGroup.add(dubboRadioButton);

        projectFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(
                FileChooserDescriptorFactory.createSingleFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                projectFolderBtn.setText(projectFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });

        historyList.addListSelectionListener(e -> {
            String selectedValue = historyList.getSelectedValue();
            if (selectedValue == null) {
                return;
            }

            historySelectValue = selectedValue;
            JsonObject jsonObject = generateHelper.getConfig(historySelectValue);

            loadConfig(jsonObject);
        });

        deleteHistoryBtn.addActionListener(e -> {
            if (historySelectValue == null) {
                return;
            }

            int res = Messages.showYesNoDialog("确定删除" + historySelectValue + "?", "Tip", null);
            if (res != Messages.OK) {
                return;
            }

            ApplicationManager.getApplication().runWriteAction(() -> generateHelper.removeConfig(historySelectValue));

            initHistoryList();

            Messages.showInfoMessage("删除历史配置" + historySelectValue + "成功!", "Tip");

            historySelectValue = null;
        });

        initHistoryList();

        projectFolderBtn.setText(project.getBasePath());
        apiFolderBtn.setText(project.getBasePath());
        apiPanel.setVisible(false);

        initChoosePackageBtn(chooseControllerPackageBtn, controllerPackageTextField);

        initChoosePackageBtn(chooseServicePackageBtn, servicePackageTextField);

        initChoosePackageBtn(choosePojoPackageBtn, pojoPackageTextField);
    }

    @SneakyThrows
    private void loadConfig() {
        JsonObject jsonObject = generateHelper.getFirstConfig();

        loadConfig(jsonObject);
    }

    @SneakyThrows
    private void loadConfig(@Nullable JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (!jsonObject.has(fieldName)) {
                continue;
            }

            Object fieldObj = field.get(this);
            Class<?> fieldObjClass = fieldObj.getClass();
            if (fieldObjClass == JBTextField.class) {
                Method method = fieldObjClass.getMethod("setText", String.class);
                method.setAccessible(true);
                method.invoke(fieldObj, jsonObject.get(fieldName).getAsString());
            } else if (fieldObjClass == TextFieldWithBrowseButton.class) {
                Method method = fieldObjClass.getDeclaredMethod("setText", String.class);
                method.setAccessible(true);
                method.invoke(fieldObj, jsonObject.get(fieldName).getAsString());
            } else if (fieldObjClass == JBRadioButton.class) {
                Method method = fieldObjClass.getMethod("setSelected", boolean.class);
                method.setAccessible(true);
                method.invoke(fieldObj, jsonObject.get(fieldName).getAsBoolean());
            }
        }

        if (feignRadioButton.getSelectedObjects() != null | dubboRadioButton.getSelectedObjects() != null) {
            apiPanel.setVisible(true);
        }
    }

    @SneakyThrows
    private JsonObject createConfig() {
        JsonObject jsonObject = new JsonObject();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            Object fieldObj = field.get(this);
            if (fieldObj == null) {
                continue;
            }

            Class<?> fieldObjClass = fieldObj.getClass();
            if (fieldObjClass == JBTextField.class) {
                Method method = fieldObjClass.getMethod("getText");
                method.setAccessible(true);
                String value = (String) method.invoke(fieldObj);
                jsonObject.addProperty(fieldName, value);
            } else if (fieldObjClass == TextFieldWithBrowseButton.class) {
                Method method = fieldObjClass.getDeclaredMethod("getText");
                method.setAccessible(true);
                String value = (String) method.invoke(fieldObj);
                jsonObject.addProperty(fieldName, value);
            } else if (fieldObjClass == JBRadioButton.class) {
                Method method = fieldObjClass.getMethod("getSelectedObjects");
                method.setAccessible(true);
                Object invoke = method.invoke(fieldObj);
                jsonObject.addProperty(fieldName, invoke != null);
            }
        }

        return jsonObject;
    }

    private void initChoosePackageBtn(JButton button, JBTextField textField) {
        button.addActionListener(actionEvent -> {
            PackageChooserDialog chooser = new PackageChooserDialog("Choose Package", project);
            chooser.selectPackage(textField.getText());
            chooser.show();
            PsiPackage psiPackage = chooser.getSelectedPackage();
            if (psiPackage == null) {
                return;
            }

            String packageName = psiPackage.getQualifiedName();
            textField.setText(packageName);
        });
    }

    private void initHistoryList() {
        Set<String> configNames = generateHelper.getConfigNames();

        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        for (String key : configNames) {
            defaultListModel.addElement(key);
        }

        historyList.setModel(defaultListModel);
    }

    @Override
    protected void doOKAction() {
        JsonObject jsonObject = createConfig();

        List<String> errors = generateHelper.validateParams(jsonObject);
        if (!errors.isEmpty()) {
            Messages.showErrorDialog(String.join("\n", errors), "Tip");
            return;
        }

        super.doOKAction();

        ApplicationManager.getApplication().runWriteAction(() -> generateHelper.saveAndGenerate(jsonObject, true));
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

}
