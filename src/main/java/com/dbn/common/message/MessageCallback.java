package com.dbn.common.message;

import com.dbn.common.routine.Consumer;
import org.jetbrains.annotations.Nullable;

public interface MessageCallback extends Consumer<Integer> {

    static void when(boolean condition, @Nullable Runnable runnable) {
        if (condition && runnable != null) {
            runnable.run();
        }
    }
}
