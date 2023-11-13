package com.dbn.database.interfaces.queue;

public enum InterfaceTaskStatus implements Status{
    NEW,
    QUEUED,
    DEQUEUED,
    SCHEDULED,
    STARTED,
    FINISHED,
    RELEASED,
    CANCELLED;

    public Status getPair() {
        switch (this) {
            case DEQUEUED: return QUEUED;
            case FINISHED: return STARTED;
            default: return null;
        }
    }

    @Override
    public boolean isRightAfter(Status status) {
        return ordinal() - status.ordinal() == 1;
    }

    @Override
    public boolean isAfter(Status status) {
        return ordinal() > status.ordinal();
    }
}
