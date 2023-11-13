package com.dbn.connection.config;

import com.dbn.common.routine.Consumer;
import com.dbn.common.util.Safe;
import com.dbn.connection.ConnectionId;

public final class ConnectionConfigAdapter implements ConnectionConfigListener {
    private Runnable changesConsumer;
    private Consumer<ConnectionId> removeConsumer;
    private Consumer<ConnectionId> changeConsumer;
    private Consumer<ConnectionId> nameChangeConsumer;


    @Override
    public void connectionsChanged() {
        Safe.run(changesConsumer, c -> c.run());
    }

    @Override
    public void connectionRemoved(ConnectionId connectionId) {
        Safe.run(removeConsumer, c -> c.accept(connectionId));
    }

    @Override
    public void connectionChanged(ConnectionId connectionId) {
        Safe.run(changeConsumer, c -> c.accept(connectionId));
    }

    @Override
    public void connectionNameChanged(ConnectionId connectionId) {
        Safe.run(nameChangeConsumer, c -> c.accept(connectionId));
    }

    public ConnectionConfigAdapter whenSetupChanged(Runnable changesConsumer) {
        this.changesConsumer = changesConsumer;
        return this;
    }

    public ConnectionConfigAdapter whenRemoved(Consumer<ConnectionId> removeConsumer) {
        this.removeConsumer = removeConsumer;
        return this;
    }

    public ConnectionConfigAdapter whenChanged(Consumer<ConnectionId> changeConsumer) {
        this.changeConsumer = changeConsumer;
        return this;
    }

    public ConnectionConfigAdapter whenNameChanged(Consumer<ConnectionId> nameChangeConsumer) {
        this.nameChangeConsumer = nameChangeConsumer;
        return this;
    }
}
