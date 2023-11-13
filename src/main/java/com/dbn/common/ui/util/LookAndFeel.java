package com.dbn.common.ui.util;

import com.intellij.util.ui.UIUtil;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import javax.swing.*;
import java.util.Objects;

@UtilityClass
public final class LookAndFeel {
    @Getter
    static boolean darkMode = UIUtil.isUnderDarcula();

    static {
        UIManager.addPropertyChangeListener(evt -> {
            if (Objects.equals(evt.getPropertyName(), "lookAndFeel")) {
                LookAndFeel.darkMode = UIUtil.isUnderDarcula();
            }


        });
    }


}
