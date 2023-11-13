package com.dbn.common.action;

import com.dbn.common.compatibility.Compatibility;

@Compatibility
public interface BackgroundUpdatedAction {

    @Compatibility
    default boolean isUpdateInBackground() {
        return true;
    }

}
