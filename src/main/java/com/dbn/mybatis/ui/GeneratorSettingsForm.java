package com.dbn.mybatis.ui;

import com.dbn.mybatis.DbnMyBatisGenerator;
import com.dbn.mybatis.model.Config;
import com.dbn.mybatis.custom.CustomPluginHandler;
import com.dbn.mybatis.settings.GeneratorSettings;
import com.dbn.object.DBTable;
import com.google.common.collect.Maps;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneratorSettingsForm {
    private JBList<String> historyList;
    private JButton deleteHistoryBtn;
    private JButton chooseModelPackageBtn;
    private TextFieldWithBrowseButton projectFolderBtn;
    private JBTextField modelPackageTextField;
    private JBTextField modelMvnPathTextField;
    private JButton chooseEnumPackageBtn;
    private JBTextField enumPostfixTextField;
    private JBTextField enumPackageTextField;
    private JBTextField enumMvnPathTextField;
    private JBTextField javaMapperPackageTextField;
    private JBTextField javaMapperMvnPathTextField;
    private JBTextField xmlFolderTextField;
    private JBTextField xmlMvnPathTextField;
    private JCheckBox commentCheckBox;
    private JCheckBox useLombokCheckBox;
    private JCheckBox integerTinyintCheckBox;
    private JCheckBox overrideJavaCheckBox;
    private JCheckBox overrideXmlCheckBox;
    private JCheckBox jpaAnnotationCheckBox;
    private JCheckBox toStringHashCodeEqualsCheckBox;
    private JCheckBox useExampleCheckBox;
    private JCheckBox generateEnumCheckBox;
    private JButton chooseJavaMapperPackageBtn;

    @Getter
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JBTextField nameTextField;
    private JCheckBox serializableCheckBox;
    private JCheckBox rowBoundsCheckBox;
    private JCheckBox mapperAnnotationCheckBox;
    private JCheckBox staticFieldNameCheckBox;
    private JPanel namePanel;
    private JCheckBox tkMapperCheckBox;
    private JButton customPluginButton;
    private JCheckBox bigintIntegerCheckBox;

    public static final String INIT_CONFIG_NAME = "initConfig";
    private Project project;
    private GeneratorSettings settings;
    private Map<String, Config> initConfigMap;
    private Map<String, Config> historyConfigMap;
    private boolean fromConfigPage;
    private DBTable dbTable;
    private String historySelectValue;

    public GeneratorSettingsForm() {
        customPluginButton.addActionListener(e -> {
            CustomPluginHandler customPluginHandler = new CustomPluginHandler(project);
            customPluginHandler.show();
        });
    }

    public void initForm(GeneratorSettings settings, DBTable dbTable) {
        project = settings.getProject();
        this.settings = settings;
        this.dbTable = dbTable;
        initConfigMap = settings.getInitConfigMap();
        historyConfigMap = settings.getHistoryConfigMap();

        fromConfigPage = dbTable == null;

        if (fromConfigPage) {
            leftPanel.getParent().remove(leftPanel);

            namePanel.getParent().remove(namePanel);
        } else {
            mainPanel.setBorder(BorderFactory.createTitledBorder("MyBatis Generator " + dbTable.getName()));
        }

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
            Config config = historyConfigMap.get(historySelectValue);
            initContent(config);
        });

        deleteHistoryBtn.addActionListener(e -> {
            if (historySelectValue == null) {
                return;
            }

            int res = Messages.showYesNoDialog("确定删除" + historySelectValue + "?", "Tip", null);
            if (res != Messages.OK) {
                return;
            }

            historyConfigMap.remove(historySelectValue);
            settings.setHistoryConfigMap(historyConfigMap);

            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            for (String key : historyConfigMap.keySet()) {
                defaultListModel.addElement(key);
            }
            historyList.setModel(defaultListModel);

            Messages.showInfoMessage("删除历史配置" + historySelectValue + "成功!", "Tip");

            historySelectValue = null;
        });

        initChoosePackageBtn(chooseModelPackageBtn, modelPackageTextField);

        initChoosePackageBtn(chooseEnumPackageBtn, enumPackageTextField);

        initChoosePackageBtn(chooseJavaMapperPackageBtn, javaMapperPackageTextField);

        initContent();
    }

    private void initContent() {
        projectFolderBtn.setText(project.getBasePath());

        Config config = null;
        if (initConfigMap != null) {
            config = initConfigMap.get(INIT_CONFIG_NAME);
        }

        historyConfigMap = settings.getHistoryConfigMap();
        if (historyConfigMap != null && !historyConfigMap.isEmpty()) {
            config = historyConfigMap.values().iterator().next();
        }

        initContent(config);
    }

    private void initContent(@Nullable Config config) {
        if (config == null) {
            return;
        }

        if (!fromConfigPage && historyConfigMap != null) {
            DefaultListModel<String> defaultListModel = new DefaultListModel<>();
            for (String key : historyConfigMap.keySet()) {
                defaultListModel.addElement(key);
            }
            historyList.setModel(defaultListModel);
        }

        projectFolderBtn.setText(config.getProjectFolder());
        nameTextField.setText(config.getName());

        modelPackageTextField.setText(config.getModelPackage());
        modelMvnPathTextField.setText(config.getModelMvnPath());

        javaMapperPackageTextField.setText(config.getJavaMapperPackage());
        javaMapperMvnPathTextField.setText(config.getJavaMapperMvnPath());

        enumPostfixTextField.setText(config.getEnumPostfixPackage());
        enumPackageTextField.setText(config.getEnumPackage());
        enumMvnPathTextField.setText(config.getEnumMvnPath());

        xmlFolderTextField.setText(config.getXmlFolder());
        xmlMvnPathTextField.setText(config.getXmlMvnPath());

        commentCheckBox.setSelected(config.isComment());
        generateEnumCheckBox.setSelected(config.isGenerateEnum());
        overrideXmlCheckBox.setSelected(config.isOverrideXML());
        overrideJavaCheckBox.setSelected(config.isOverrideJava());
        integerTinyintCheckBox.setSelected(config.isIntegerTinyInt());
        bigintIntegerCheckBox.setSelected(config.isIntegerBigint());
        toStringHashCodeEqualsCheckBox.setSelected(config.isNeedToStringHashcodeEquals());
        jpaAnnotationCheckBox.setSelected(config.isJpaAnnotation());
        useExampleCheckBox.setSelected(config.isUseExample());
        useLombokCheckBox.setSelected(config.isUseLombokPlugin());
        serializableCheckBox.setSelected(config.isSerializable());
        rowBoundsCheckBox.setSelected(config.isRowBounds());
        mapperAnnotationCheckBox.setSelected(config.isMapperAnnotation());
        staticFieldNameCheckBox.setSelected(config.isStaticFieldName());
        tkMapperCheckBox.setSelected(config.isTkMapper());
    }

    private Config createConfig(String configName) {
        Config config = new Config();
        config.setName(configName);

        config.setProjectFolder(projectFolderBtn.getText());

        config.setModelPackage(modelPackageTextField.getText());
        config.setModelMvnPath(modelMvnPathTextField.getText());

        config.setJavaMapperPackage(javaMapperPackageTextField.getText());
        config.setJavaMapperMvnPath(javaMapperMvnPathTextField.getText());

        config.setEnumPostfixPackage(enumPostfixTextField.getText());
        config.setEnumPackage(enumPackageTextField.getText());
        config.setEnumMvnPath(enumMvnPathTextField.getText());

        config.setXmlFolder(xmlFolderTextField.getText());
        config.setXmlMvnPath(xmlMvnPathTextField.getText());

        config.setComment(commentCheckBox.getSelectedObjects() != null);
        config.setOverrideXML(overrideXmlCheckBox.getSelectedObjects() != null);
        config.setOverrideJava(overrideJavaCheckBox.getSelectedObjects() != null);
        config.setNeedToStringHashcodeEquals(toStringHashCodeEqualsCheckBox.getSelectedObjects() != null);
        config.setJpaAnnotation(jpaAnnotationCheckBox.getSelectedObjects() != null);
        config.setUseExample(useExampleCheckBox.getSelectedObjects() != null);
        config.setUseLombokPlugin(useLombokCheckBox.getSelectedObjects() != null);
        config.setGenerateEnum(generateEnumCheckBox.getSelectedObjects() != null);
        config.setIntegerTinyInt(integerTinyintCheckBox.getSelectedObjects() != null);
        config.setIntegerBigint(bigintIntegerCheckBox.getSelectedObjects() != null);
        config.setSerializable(serializableCheckBox.getSelectedObjects() != null);
        config.setRowBounds(rowBoundsCheckBox.getSelectedObjects() != null);
        config.setMapperAnnotation(mapperAnnotationCheckBox.getSelectedObjects() != null);
        config.setStaticFieldName(staticFieldNameCheckBox.getSelectedObjects() != null);
        config.setTkMapper(tkMapperCheckBox.getSelectedObjects() != null);

        return config;
    }

    public void reset() {
        initContent();
    }

    public void apply() {
        Config config = createConfig(INIT_CONFIG_NAME);

        Map<String, Config> initConfigMap = Maps.newLinkedHashMap();
        initConfigMap.put(config.getName(), config);

        settings.setInitConfigMap(initConfigMap);
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

    public List<String> validateParams() {
        Config config = createConfig("temp");
        Field[] fields = config.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(it -> {
                    it.setAccessible(true);
                    Class<?> declaringClass = it.getType();
                    if (declaringClass != String.class) {
                        return null;
                    }

                    String value;
                    try {
                        value = (String) it.get(config);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    if (StringUtils.isNotBlank(value)) {
                        return null;
                    }

                    return it.getName() + "不能为空";
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @SneakyThrows
    public void saveAndGenerate() {
        if (historyConfigMap == null) {
            historyConfigMap = Maps.newLinkedHashMap();
        }

        String name = nameTextField.getText();
        Config config = createConfig(name);

        historyConfigMap.put(config.getName(), config);
        settings.setHistoryConfigMap(historyConfigMap);

        DbnMyBatisGenerator dbnMyBatisGenerator = DbnMyBatisGenerator.createInstance(dbTable, config);

        dbnMyBatisGenerator.generator();
    }
}
