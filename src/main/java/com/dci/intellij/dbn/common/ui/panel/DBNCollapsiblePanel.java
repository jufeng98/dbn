package com.dci.intellij.dbn.common.ui.panel;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.ui.component.DBNComponent;
import com.dci.intellij.dbn.common.ui.form.DBNFormBase;
import com.dci.intellij.dbn.common.ui.util.Mouse;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DBNCollapsiblePanel<P extends DBNComponent> extends DBNFormBase {
    private JLabel collapseExpandLabel;
    private JPanel contentPanel;
    private JPanel mainPanel;
    private boolean expanded;

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public DBNCollapsiblePanel(@NotNull P parent, JComponent contentComponent, String title, boolean expanded) {
        super(parent);
        this.expanded = expanded;
        contentPanel.add(contentComponent, BorderLayout.CENTER);
        contentPanel.setVisible(this.expanded);
        collapseExpandLabel.setText(title);
        collapseExpandLabel.setIcon(this.expanded ? Icons.COMMON_DOWN : Icons.COMMON_RIGHT);
        collapseExpandLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        collapseExpandLabel.setForeground(Colors.HINT_COLOR);

        collapseExpandLabel.addMouseListener(Mouse.listener().onClick(e -> {
            if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                DBNCollapsiblePanel.this.expanded = !DBNCollapsiblePanel.this.expanded;
                contentPanel.setVisible(DBNCollapsiblePanel.this.expanded);
                collapseExpandLabel.setIcon(DBNCollapsiblePanel.this.expanded ? Icons.COMMON_DOWN : Icons.COMMON_RIGHT);
            }
        }));
    }
}
