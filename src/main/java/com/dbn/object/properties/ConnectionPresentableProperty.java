package com.dbn.object.properties;

import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.intellij.pom.Navigatable;
import lombok.EqualsAndHashCode;

import javax.swing.*;

@EqualsAndHashCode(callSuper = false)
public class ConnectionPresentableProperty extends PresentableProperty{
    private final ConnectionRef connection;

    public ConnectionPresentableProperty(ConnectionHandler connection) {
        this.connection = connection.ref();
    }

    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    @Override
    public String getName() {
        return "Connection";
    }

    @Override
    public String getValue() {
        return getConnection().getName();
    }

    @Override
    public Icon getIcon() {
        return getConnection().getIcon();
    }

    @Override
    public Navigatable getNavigatable() {
        return getConnection().getObjectBundle();
    }
}
