package com.dci.intellij.dbn.common.thread;

import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.routine.ThrowableRunnable;
import com.intellij.openapi.project.Project;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

public class Callback{
    private Runnable before;
    private Runnable success;
    private Consumer<Exception> failure;
    private Runnable after;

    public static Callback create() {
        return new Callback();
    }

    public void before(Runnable before) {
        this.before = before;
    }

    public void onSuccess(Runnable success) {
        this.success = success;
    }

    public void onFailure(Consumer<Exception> failure) {
        this.failure = failure;
    }

    public void after(Runnable after) {
        this.after = after;
    }

    public void background(Project project, ThrowableRunnable<Exception> action) {
        Background.run(project, () -> surround(action));
    }

    public void surround(ThrowableRunnable<Exception> action) {
        try {
            guarded(before, b -> b.run());
            guarded(action);
            guarded(success, s -> s.run());
        } catch (Exception e) {
            conditionallyLog(e);
            if (failure != null) guarded(failure, f -> f.accept(e));
        } finally {
            guarded(after, a -> a.run());
        }
    }


}
