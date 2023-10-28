package com.dci.intellij.dbn.common.ui;

import com.intellij.ide.IdeTooltip;
import com.intellij.ide.TooltipEvent;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

@Setter
public class DBNTooltip extends IdeTooltip {
    private Boolean dismissOnTimeout;
    private Integer dismissDelay;

    public DBNTooltip(Component component, Point point, JComponent tipComponent, Object... identity) {
        super(component, point, tipComponent, identity);
    }

    @Override
    public boolean canBeDismissedOnTimeout() {
        return dismissOnTimeout == null ? super.canBeDismissedOnTimeout() : dismissOnTimeout;
    }

    @Override
    public int getDismissDelay() {
        return dismissDelay == null ? super.getDismissDelay() : dismissDelay;
    }

    @Override
    protected boolean canAutohideOn(TooltipEvent event) {
        InputEvent inputEvent = event.getInputEvent();
        if (inputEvent == null) return false;
        if (inputEvent instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) inputEvent;
            return mouseEvent.getID() != MouseEvent.MOUSE_MOVED;
        }

        return true;
    }
}
