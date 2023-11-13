package com.dbn.common.ui.util;

import javax.swing.*;
import java.awt.*;

public interface Borderless {

    static void markBorderless(Component component) {
        ClientProperty.BORDERLESS.set(component, Boolean.TRUE);
    }

    static boolean isBorderless(Component component) {
        Boolean borderless = ClientProperty.BORDERLESS.get(component);
        if (borderless != null) return borderless;
        if (component instanceof JPanel) return true;
        if (component instanceof Borderless) return true;

        return false;
    }
}
