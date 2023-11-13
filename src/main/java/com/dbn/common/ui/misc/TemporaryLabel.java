package com.dbn.common.ui.misc;

import com.dbn.common.util.Timers;
import com.dbn.common.thread.Dispatch;

import javax.swing.*;

public class TemporaryLabel extends JLabel {

    public void show(int timeout) {
        changeVisibility(true);
        Timers.executeLater("TemporaryLabelTimeout", timeout, () -> changeVisibility(false));
    }

    private void changeVisibility(boolean aFlag) {
        Dispatch.run(() -> setVisible(aFlag));
    }
}
