package com.dbn.object.factory.ui.common;

import com.dbn.common.ui.component.DBNComponent;
import com.dbn.common.ui.form.DBNFormBase;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.object.type.DBObjectType;
import com.dbn.object.factory.ObjectFactoryInput;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@Getter
@Setter
public abstract class ObjectFactoryInputForm<T extends ObjectFactoryInput> extends DBNFormBase {
    private int index;
    private final ConnectionRef connection;
    private final DBObjectType objectType;

    protected ObjectFactoryInputForm(@NotNull DBNComponent parent, @NotNull ConnectionHandler connection, DBObjectType objectType, int index) {
        super(parent);
        this.connection = connection.ref();
        this.objectType = objectType;
        this.index = index;
    }

    @NotNull
    @Override
    public abstract JPanel getMainComponent();

    @NotNull
    public ConnectionHandler getConnection() {
        return connection.ensure();
    }

    public abstract T createFactoryInput(ObjectFactoryInput parent);

    public abstract void focus();
}
