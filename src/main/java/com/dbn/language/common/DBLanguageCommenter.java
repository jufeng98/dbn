package com.dbn.language.common;

public class DBLanguageCommenter implements com.intellij.lang.Commenter {

    public static final DBLanguageCommenter COMMENTER = new DBLanguageCommenter();

    @Override
    public String getLineCommentPrefix() {
        return "--";
    }

    public boolean isLineCommentPrefixOnZeroColumn() {
        return false;
    }

    @Override
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }
}
