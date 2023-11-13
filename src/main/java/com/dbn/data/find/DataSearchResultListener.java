package com.dbn.data.find;

import java.util.EventListener;

public interface DataSearchResultListener extends EventListener {
    void searchResultUpdated(DataSearchResult searchResult);
}
