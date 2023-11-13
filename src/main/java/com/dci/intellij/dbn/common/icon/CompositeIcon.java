package com.dci.intellij.dbn.common.icon;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CompositeIcon implements Icon {

    private final Icon myLeftIcon;
    private final int myHorizontalStrut;
    private final Icon myRightIcon;

    public CompositeIcon(@NotNull Icon leftIcon, int horizontalStrut, @NotNull Icon rightIcon) {
      myLeftIcon = leftIcon;
      myHorizontalStrut = horizontalStrut;
      myRightIcon = rightIcon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      paintIconAlignedCenter(c, g, x, y, myLeftIcon);
      paintIconAlignedCenter(c, g, x + myLeftIcon.getIconWidth() + myHorizontalStrut, y, myRightIcon);
    }

    private void paintIconAlignedCenter(Component c, Graphics g, int x, int y, @NotNull Icon icon) {
      int iconHeight = getIconHeight();
      icon.paintIcon(c, g, x, y + (iconHeight - icon.getIconHeight()) / 2);
    }

    @Override
    public int getIconWidth() {
      return myLeftIcon.getIconWidth() + myHorizontalStrut + myRightIcon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
      return Math.max(myLeftIcon.getIconHeight(), myRightIcon.getIconHeight());
    }
  }