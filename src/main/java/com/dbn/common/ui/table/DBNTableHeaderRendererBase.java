package com.dbn.common.ui.table;

import com.dbn.common.latent.Latent;

import javax.swing.*;
import java.awt.*;

public abstract class DBNTableHeaderRendererBase implements DBNTableHeaderRenderer {

    private final Latent<FontMetrics> fontMetrics = Latent.basic(() -> {
        JLabel nameLabel = getNameLabel();
        return nameLabel.getFontMetrics(nameLabel.getFont());
    });

    protected abstract JLabel getNameLabel();

    @Override
    public final void setFont(Font font) {
        getNameLabel().setFont(font);
        fontMetrics.reset();
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics.get();
    }
}

