package com.dbn.object.common.ui;

import com.dbn.common.ui.util.Fonts;
import com.dbn.connection.ConnectionHandler;
import com.dbn.object.common.DBObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ObjectDetailsForm {
    private JLabel connectionLabel;
    private JPanel objectPanel;
    private JPanel mainPanel;
    private DBObject object;

    public ObjectDetailsForm(DBObject object) {
        this.object = object;
        objectPanel.setLayout(new BoxLayout(objectPanel, BoxLayout.X_AXIS));
        ConnectionHandler connection = object.getConnection();
        connectionLabel.setText(connection.getName());
        connectionLabel.setIcon(connection.getIcon());
        

        java.util.List<DBObject> chain = new ArrayList<>();
        while (object != null) {
            chain.add(0, object);
            object = object.getParentObject();
        }

        for (int i=0; i<chain.size(); i++) {
            object = chain.get(i);
            if ( i > 0) objectPanel.add(new JLabel(" > "));

            JLabel objectLabel = new JLabel(object.getName(), object.getIcon(), SwingConstants.LEFT);
            if (object == this.object) {
                Font font = Fonts.deriveFont(objectLabel.getFont(), Font.BOLD);
                objectLabel.setFont(font);
            }
            objectPanel.add(objectLabel);
        }

    }

    public JPanel getComponent() {
        return mainPanel;
    }
}
