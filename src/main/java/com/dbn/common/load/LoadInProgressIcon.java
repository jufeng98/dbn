package com.dbn.common.load;

import com.dbn.common.util.TimeUtil;
import com.dbn.common.dispose.Disposer;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class LoadInProgressIcon implements Icon{
    public static final Icon INSTANCE = new LoadInProgressIcon();

    public static int ROLL_INTERVAL = 50;
    private static int ROLL_ELEMENTS = 12;

    private static final Icon[] ICONS;
    static {
        try {
            // TODO workaround for spinner change from 12 to 8 (go standard api if possible)
            Field field = Class.forName("com.intellij.icons.AllIcons$Process").getDeclaredField("Step_9");
            if (field.getAnnotation(Deprecated.class) != null) {
                ROLL_INTERVAL = 80;
                ROLL_ELEMENTS = 8;
            }
        } catch (Throwable e) {
            conditionallyLog(e);
        }

        ICONS = new Icon[ROLL_ELEMENTS];
        for (int i = 0; i < ICONS.length; i++) {
            ICONS[i] = IconLoader.getIcon("/process/step_" + (i + 1) + ".png");
        }
    }

    private static int iconIndex;
    private static long lastAccessTimestamp = System.currentTimeMillis();

    private static volatile Timer ICON_ROLLER;
    private static class IconRollerTimerTask extends TimerTask {
        @Override
        public void run() {
            if (iconIndex == ICONS.length - 1) {
                iconIndex = 0;
            } else {
                iconIndex++;
            }

            if (ICON_ROLLER != null && TimeUtil.isOlderThan(lastAccessTimestamp, TimeUtil.Millis.TEN_SECONDS)) {
                synchronized (IconRollerTimerTask.class) {
                    Timer cachedIconRoller = ICON_ROLLER;
                    ICON_ROLLER = null;
                    Disposer.dispose(cachedIconRoller);
                }
            }
        }
    };

    private static void startRoller() {
        if (ICON_ROLLER == null) {
            synchronized (IconRollerTimerTask.class) {
                if (ICON_ROLLER == null) {
                    ICON_ROLLER = new Timer("DBN - Load in Progress (icon roller)");
                    ICON_ROLLER.schedule(new IconRollerTimerTask(), ROLL_INTERVAL, ROLL_INTERVAL);
                }
            }
        }
    }

    private static Icon getIcon() {
        startRoller();
        lastAccessTimestamp = System.currentTimeMillis();
        return ICONS[iconIndex];
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        getIcon().paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return ICONS[0].getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return ICONS[0].getIconHeight();
    }
}
