package com.dci.intellij.dbn.common.thread;

import com.dci.intellij.dbn.common.routine.Consumer;
import com.dci.intellij.dbn.common.routine.ThrowableCallable;
import com.dci.intellij.dbn.common.util.Commons;
import com.dci.intellij.dbn.diagnostics.Diagnostics;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.util.Alarm;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static com.dci.intellij.dbn.common.dispose.Failsafe.guarded;
import static com.dci.intellij.dbn.common.dispose.Failsafe.nd;

@UtilityClass
public final class Dispatch {

    public static void run(Runnable runnable) {
        run(null, runnable);
    }

    public static void run(boolean conditional, Runnable runnable) {
        if (conditional && ThreadMonitor.isDispatchThread()) {
            guarded(runnable, r -> r.run());
        } else {
            run(null, runnable);
        }
    }

    public static void run(ModalityState modalityState, Runnable runnable) {
        Application application = ApplicationManager.getApplication();
        modalityState = Commons.nvl(modalityState, application.getDefaultModalityState());
        application.invokeLater(() -> guarded(() -> runnable.run()), modalityState/*, ModalityState.NON_MODAL*/);
    }

    public static <T, E extends Throwable> T call(boolean conditional, ThrowableCallable<T, E> callable) throws E{
        if (conditional && ThreadMonitor.isDispatchThread()) {
            return callable.call();
        } else {
            return call(callable);
        }
    }

    public static <T> void background(Project project, Supplier<T> supplier, Consumer<T> consumer) {
        Background.run(project, () -> {
            T value = supplier.get();
            run(() -> consumer.accept(value));
        });
    }

    public static <T> void background(Project project, ModalityState modalityState, Runnable loader, Runnable renderer) {
        Background.run(project, () -> {
            loader.run();
            run(modalityState, renderer);
        });
    }


    public static <T, E extends Throwable> T call(ThrowableCallable<T, E> callable) throws E{
        Application application = ApplicationManager.getApplication();
        ModalityState modalityState = application.getDefaultModalityState();
        AtomicReference<T> resultRef = new AtomicReference<>();
        AtomicReference<E> exceptionRef = new AtomicReference<>();
        application.invokeAndWait(() -> {
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
        nd(parentDisposable);
        return new Alarm(parentDisposable);
    }

    public static void delayed(int delayMillis, @NotNull Runnable runnable) {
        alarmRequest(new Alarm(), delayMillis, false, runnable);
    }

    public static void alarmRequest(@NotNull Alarm alarm, int delayMillis, boolean cancelRequests, @NotNull Runnable runnable) {
        run(true, () -> {
            if (!alarm.isDisposed()) {
                if (cancelRequests) {
                    alarm.cancelAllRequests();
                }

                if (!alarm.isDisposed()) {
                    alarm.addRequest(runnable, delayMillis);
                }
            }
        });
    }
}
