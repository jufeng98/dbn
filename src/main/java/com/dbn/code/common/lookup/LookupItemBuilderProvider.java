package com.dbn.code.common.lookup;

import com.dbn.language.common.DBLanguage;

public interface LookupItemBuilderProvider {

    LookupItemBuilder getLookupItemBuilder(DBLanguage language);

}
