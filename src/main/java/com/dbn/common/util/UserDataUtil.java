package com.dbn.common.util;

import com.dbn.common.routine.ThrowableCallable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;

public interface UserDataUtil {
    static <T, E extends Throwable> T ensure(UserDataHolder holder, Key<T> key, ThrowableCallable<T, E> loader) throws E {
        T userData = (T) holder.getUserData(key);
        if (userData == null) {
            synchronized (holder) {
                userData = (T) holder.getUserData(key);
                if (userData == null) {
                    userData = loader.call();
                    holder.putUserData(key, userData);
                }
            }
        }
        return userData;
    }
}
