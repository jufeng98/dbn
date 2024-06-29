package com.dbn.common.action;

import com.dbn.common.compatibility.Workaround;

@Workaround // DataProvider override only assertions
public interface DataProvider {
    Object getData(String name);
}
