package com.dbn.common.thread;

import com.dbn.common.dispose.Failsafe;
import com.dbn.common.routine.Consumer;
import com.dbn.common.routine.ThrowableCallable;
import com.dbn.diagnostics.Diagnostics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.util.Alarm;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.dbn.common.ui.util.UserInterface.whenShown;
import static com.dbn.common.util.Commons.nvl;
import static com.intellij.openapi.application.ApplicationManager.getApplication;

@UtilityClass
public final class Dispatch {

    public static void run(Runnable runnable) {
        run(null, runnable);
    }

    public static void run(boolean conditional, Runnable runnable) {
        if (conditional && ThreadMonitor.isDispatchThread()) {
            Failsafe.guarded(runnable, r -> r.run());
        } else {
            run(null, runnable);
        }
    }

    public static void run(ModalityState modalityState, Runnable runnable) {
        modalityState = nvl(modalityState, () -> ModalityState.defaultModalityState());
        getApplication().invokeLater(() -> Failsafe.guarded(() -> runnable.run()), modalityState);
    }

    public static <T, E extends Throwable> T call(boolean conditional, ThrowableCallable<T, E> callable) throws E{
        if (conditional && ThreadMonitor.isDispatchThread()) {
            return callable.call();
        } else {
            return call(callable);
        }
    }

    public static <T> void async(Project project, JComponent component, Supplier<T> supplier, Consumer<T> consumer) {
        if (component.isShowing()) {
            background(project, component, supplier, consumer);
            return;
        }
        // invoke when component is shown and the modality state is known
        whenShown(component, () -> background(project, component, supplier, consumer));
    }

    private static <T> void background(Project project, JComponent component, Supplier<T> supplier, Consumer<T> consumer) {
        Background.run(project, () -> {
            T value = supplier.get();
            ModalityState modalityState = ModalityState.stateForComponent(component);
            run(modalityState, () -> consumer.accept(value));
        });
    }

    public static <T, E extends Throwable> T call(ThrowableCallable<T, E> callable) throws E{
        ModalityState modalityState = ModalityState.defaultModalityState();
        AtomicReference<T> resultRef = new AtomicReference<>();
        AtomicReference<E> exceptionRef = new AtomicReference<>();
        getApplication().invokeAndWait(() -> {
            T result = null;
            try {
                result = callable.call();
                resultRef.set(result);
            } catch (Throwable e) {
                Diagnostics.conditionallyLog(e);
                exceptionRef.set((E) e);
            }

        }, modalityState);
        if (exceptionRef.get() != null) {
            throw exceptionRef.get();
        }

        return resultRef.get();
    }


    public static Alarm alarm(Disposable parentDisposable) {
        Failsafe.nd(parentDisposable);
        return new Alarm(parentDisposable);
    }

    public static void delayed(int delayMillis, @NotNull Runnable runnable) {
        alarmRequest(new Alarm(), delayMillis, false, runnable);
    }

    public static void alarmRequest(@NotNull Alarm alarm, long delayMillis, boolean cancelRequests, @NotNull Runnable runnable) {
        run(true, () -> {
            if (alarm.isDisposed()) return;
            if (cancelRequests) alarm.cancelAllRequests();
            if (alarm.isDisposed()) return;

            alarm.addRequest(runnable, delayMillis);
        });
    }


    public static boolean isModalState() {
        // return ModalityState.defaultModalityState().dominates(ModalityState.nonModal());
        return ModalityState.defaultModalityState().dominates(ModalityState.NON_MODAL);
    }
}
