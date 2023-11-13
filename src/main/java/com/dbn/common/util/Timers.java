package com.dbn.common.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;

@UtilityClass
public class Timers {

    @NotNull
    public static Timer createNamedTimer(@NonNls @NotNull String name, int delay, @NotNull ActionListener listener) {
        return new Timer(delay, listener) {
            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static void executeLater(String identifier, int delay, Runnable runnable) {
        Timer timer = createNamedTimer(identifier, delay, e -> runnable.run());
        timer.setRepeats(false);
        timer.start();
    }
}
