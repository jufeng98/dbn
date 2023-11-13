package com.dbn.code.sql.style;

import com.dbn.code.sql.style.options.SQLCodeStyleSettings;
import com.dbn.code.sql.style.options.SQLCodeStyleSettingsWrapper;
import com.dbn.code.common.style.DBLCodeStyle;
import com.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dbn.code.common.style.options.CodeStyleFormattingSettings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.Nullable;

public class SQLCodeStyle extends DBLCodeStyle {
    public static SQLCodeStyleSettings settings(@Nullable Project project) {
        CodeStyleSettings rootSettings = rootSettings(project);
        SQLCodeStyleSettingsWrapper settingsWrapper = rootSettings.getCustomSettings(SQLCodeStyleSettingsWrapper.class);
        return settingsWrapper.getSettings();
    }

    public static CodeStyleCaseSettings caseSettings(Project project) {
        return settings(project).getCaseSettings();
    }

    public static CodeStyleFormattingSettings formattingSettings(Project project) {
        return settings(project).getFormattingSettings();
    }
}
