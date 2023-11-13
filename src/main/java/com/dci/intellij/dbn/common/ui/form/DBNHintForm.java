package com.dci.intellij.dbn.common.ui.form;

import com.dci.intellij.dbn.common.color.Colors;
import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.message.MessageType;
import com.dci.intellij.dbn.common.text.MimeType;
import com.dci.intellij.dbn.common.text.TextContent;
import com.dci.intellij.dbn.common.thread.Dispatch;
import com.dci.intellij.dbn.common.ui.util.Fonts;
import com.dci.intellij.dbn.common.ui.util.LookAndFeel;
import com.dci.intellij.dbn.common.ui.util.UserInterface;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.RoundedLineBorder;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class DBNHintForm extends DBNFormBase {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel hintLabel;
    private JTextPane hintTextPane;
    private HyperlinkLabel actionLink;

    private final boolean boxed;

    public DBNHintForm(DBNForm parent, @Nullable TextContent hintContent, MessageType messageType, boolean boxed) {
        this(parent, hintContent, messageType, boxed, null, null);
    }

    public DBNHintForm(DBNForm parent, @Nullable TextContent hintContent, MessageType messageType, boolean boxed, String actionText, Runnable action) {
        super(parent);
        this.boxed = boxed;
        hintLabel.setText("");
        setMessageType(messageType);
        setHintContent(hintContent);

        Color background = getBackground();

        hintTextPane.setBackground(background);
        hintTextPane.setFont(Fonts.getLabelFont());
        contentPanel.setBackground(background);
        contentPanel.setForeground(boxed ? Colors.lafBrighter(Colors.getLabelForeground(), 1) : Colors.HINT_COLOR);
        if (boxed) {
            mainPanel.setBorder(new RoundedLineBorder(Colors.getOutlineColor(), 2));
            //mainPanel.setBorder(new RoundedLineBorder(UIManager.getColor("TextField.borderColor"), 3));
            //mainPanel.setBorder(UIUtil.getTextFieldBorder());
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) contentPanel.getLayout();
            gridLayoutManager.setMargin(JBUI.emptyInsets());
        }

        if (actionText != null) {
            actionLink.setVisible(true);
            actionLink.setHyperlinkText(actionText);
            actionLink.addHyperlinkListener(e -> action.run());
        } else {
            actionLink.setVisible(false);
        }


    }

    @SneakyThrows
    private void resizeTextPane() {
        Dispatch.run(() -> {
            Dimension preferredSize = hintTextPane.getPreferredSize();
            hintTextPane.revalidate();

            Dimension contentPreferredSize = contentPanel.getPreferredSize();
            mainPanel.setPreferredSize(UserInterface.adjust(contentPreferredSize, 0, 10));
            mainPanel.revalidate();

            Dimension contentSize = UserInterface.adjust(preferredSize, 4, 4);
            if (!preferredSize.equals(contentSize)) {
                hintTextPane.setPreferredSize(contentSize);
                hintTextPane.revalidate();
            }
        });
    }

    @NotNull
    private Color getBackground() {
        if (boxed) {
            return LookAndFeel.isDarkMode() ?
                    Colors.lafDarker(Colors.getPanelBackground(), 1) :
                    Colors.lafBrighter(Colors.getPanelBackground(), 1);
        }

        return Colors.getPanelBackground();
    }

    public void setHighlighted(boolean highlighted) {
        Color background = highlighted ? Colors.getTextFieldBackground() : getBackground();
        contentPanel.setBackground(background);
        hintTextPane.setBackground(background);
    }

    @NotNull
    @Override
    public JPanel getMainComponent() {
        return mainPanel;
    }

    public void setHintContent(@Nullable TextContent content) {
        if (content == null) {
            hintTextPane.setContentType(MimeType.TEXT_PLAIN.id());
            hintTextPane.setText("");
        } else {
            hintTextPane.setContentType(content.getTypeId());
            hintTextPane.setText(content.getText());
        }

        resizeTextPane();
    }

    public void setMessageType(MessageType messageType) {
        if (messageType == null) {
            hintLabel.setVisible(false);
            return;
        }

        Icon icon = getIcon(messageType);
        hintLabel.setIcon(icon);
    }

    private static Icon getIcon(MessageType messageType) {
        switch (messageType) {
            case INFO: return Icons.COMMON_INFO;
            case WARNING: return Icons.COMMON_WARNING;
            case ERROR: return Icons.COMMON_ERROR;
            default: return null;
        }
    }
}
