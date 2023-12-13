package com.dbn.language.sql.template;

import com.dbn.common.compatibility.Compatibility;
import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

@Deprecated
@Compatibility
public class SQLTemplateProvider implements DefaultLiveTemplatesProvider {

    private static final String[] TEMPLATES = new String[]{"com/dbn/language/sql/template/default"};

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return TEMPLATES;
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return null;
    }
}
