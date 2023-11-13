package com.dbn.common.action;

import com.dbn.common.compatibility.Compatibility;
import com.intellij.openapi.actionSystem.UpdateInBackground;

@Compatibility
public interface BackgroundUpdatedAction extends UpdateInBackground {

    @Compatibility
    default boolean isUpdateInBackground() {
        return true;
    }

}
