package com.dbn.code.common.style;

import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.code.psql.style.PSQLCodeStyle;
import com.dbn.code.sql.style.SQLCodeStyle;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLLanguage;
import com.intellij.application.options.CodeStyle;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import org.jetbrains.annotations.Nullable;

public class DBLCodeStyle {
    protected static CodeStyleSettings rootSettings(@Nullable Project project) {
        if (true) {
            return CodeStyle.getProjectOrDefaultSettings(project);
        }

        CodeStyleSettings codeStyleSettings;
        if (CodeStyleSettingsManager.getInstance().USE_PER_PROJECT_SETTINGS) {
            codeStyleSettings = CodeStyle.getSettings(project);
        } else {
            codeStyleSettings = CodeStyleSettingsManager.getInstance().getCurrentSettings();
        }
        return codeStyleSettings;
    }

    protected static DBLCodeStyleSettings<?, ?> settings(Project project, Language language) {
        if (language == SQLLanguage.INSTANCE) return SQLCodeStyle.settings(project);
        if (language == PSQLLanguage.INSTANCE) return PSQLCodeStyle.settings(project);
        throw new IllegalArgumentException("Language " + language.getID() + " mot supported");
    }
}
