package com.dbn.common.load;

import com.dbn.common.dispose.Disposer;
import com.dbn.common.util.TimeUtil;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadInProgressIcon implements Icon{
    public static final Icon INSTANCE = new LoadInProgressIcon();

    public static int ROLL_INTERVAL = 80;

    private static final Icon[] ICONS;
    static {
        ICONS = new Icon[8];
        for (int i = 0; i < ICONS.length; i++) {
            switch (i) {
                case 0: ICONS[i] = AllIcons.Process.Step_1; break;
                case 1: ICONS[i] = AllIcons.Process.Step_2; break;
                case 2: ICONS[i] = AllIcons.Process.Step_3; break;
                case 3: ICONS[i] = AllIcons.Process.Step_4; break;
                case 4: ICONS[i] = AllIcons.Process.Step_5; break;
                case 5: ICONS[i] = AllIcons.Process.Step_6; break;
                case 6: ICONS[i] = AllIcons.Process.Step_7; break;
                case 7: ICONS[i] = AllIcons.Process.Step_8; break;
            }
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
