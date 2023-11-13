package com.dbn.common.ui.misc;

import com.dbn.common.ui.util.Borderless;
import com.dbn.common.ui.util.ClientProperty;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.border.Border;
import java.awt.*;

public class DBNScrollPane extends JBScrollPane {


    public DBNScrollPane() {
    }

    public DBNScrollPane(Component view) {
        super(view);
    }

    @Override
    public void setViewportView(Component view) {
        super.setViewportView(view);
        if (view == null) return;

        adjustBackground();
        view.addPropertyChangeListener("background", e -> adjustBackground());
    }

    private void adjustBackground() {
        Component component = getViewComponent();
        if (component == null) return;

        Color background = component.getBackground();
        viewport.setBackground(background);
        setBackground(background);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }

    @Override
    public void setBorder(Border border) {
        Component component = getViewComponent();

        Border clientBorder = ClientProperty.BORDER.get(component);
        if (clientBorder != null) border = clientBorder;
        if (Borderless.isBorderless(component)) border = null;

        super.setBorder(border);
    }

    protected Component getViewComponent() {
        return getViewport().getView();
    }



}
