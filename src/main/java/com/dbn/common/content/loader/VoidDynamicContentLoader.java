package com.dbn.common.content.loader;

import com.dbn.common.content.DynamicContent;

import java.sql.SQLException;

public class VoidDynamicContentLoader implements DynamicContentLoader{

    public static final VoidDynamicContentLoader INSTANCE = new VoidDynamicContentLoader();

    private VoidDynamicContentLoader() {

    }

    @Override
    public void loadContent(DynamicContent content) throws SQLException {
        // do nothing
    }
}
