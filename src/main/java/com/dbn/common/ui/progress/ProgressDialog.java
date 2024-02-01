package com.dbn.common.ui.progress;

import com.dbn.common.color.Colors;
import com.dbn.common.ref.WeakRef;
import com.dbn.common.ui.dialog.DBNDialog;
import com.dbn.common.ui.util.Borders;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends DBNDialog<ProgressDialogForm> {
    private final WeakRef<ProgressDialogHandler> handler;

    public ProgressDialog(ProgressDialogHandler handler) {
        super(handler.getProject(), handler.getTitle(), false);
        this.handler = WeakRef.of(handler);
        setModal(false);
        setResizable(false);
        init();

        Window window = getPeer().getWindow();
        if (window instanceof JDialog) {
            JDialog dialog = (JDialog) window;
            dialog.setUndecorated(true);

            Container contentPane = dialog.getContentPane();
            if (contentPane instanceof JComponent) {
                JComponent component = (JComponent) contentPane;
                component.setBorder(Borders.lineBorder(Colors.getOutlineColor()));
            }
        }
    }

    public ProgressDialogHandler getHandler() {
        return handler.get();
    }

    @Override
    protected String getDimensionServiceKey() {
        return null;
    }

    @NotNull
    @Override
    protected DialogStyle getStyle() {
        return DialogStyle.NO_STYLE;
    }

    @Override
    protected @NotNull ProgressDialogForm createForm() {
        ProgressDialogHandler handler = getHandler();
        return new ProgressDialogForm(this, handler.getTitle(), handler.getText());
    }

    @Override
    protected Action @NotNull [] createActions() {
        //return new Action[]{new BackgroundAction(), getCancelAction()};
        return new Action[0];
    }

    @Override
    public void doCancelAction() {
        getHandler().cancel();
        close(CANCEL_EXIT_CODE);
    }

    public void doBackgroundAction() {
        close(OK_EXIT_CODE);
    }
}
