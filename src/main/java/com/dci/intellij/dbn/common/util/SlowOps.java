package com.dci.intellij.dbn.common.util;

import com.dci.intellij.dbn.common.action.UserDataKeys;
import com.dci.intellij.dbn.common.thread.ThreadMonitor;
import com.intellij.openapi.util.UserDataHolder;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@UtilityClass
public class SlowOps {

    public static <T extends UserDataHolder> boolean checkValid(@Nullable T entity, Predicate<T> verifier) {
        if (entity == null) return false;

        Boolean invalidEntity = entity.getUserData(UserDataKeys.INVALID_ENTITY);
        if (invalidEntity != null && invalidEntity) return false;

        if (ThreadMonitor.isDispatchThread()) return true;
        boolean valid = verifier.test(entity);

        if (!valid) entity.putUserData(UserDataKeys.INVALID_ENTITY, true);
        return valid;
    }
}
