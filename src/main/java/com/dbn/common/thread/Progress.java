package com.dbn.common.thread;

import com.dbn.common.dispose.Checks;
import com.dbn.common.dispose.Failsafe;
import com.dbn.common.util.Titles;
import com.dbn.connection.context.DatabaseContext;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Failsafe.guarded;
import static com.intellij.openapi.progress.PerformInBackgroundOption.ALWAYS_BACKGROUND;
import static com.intellij.openapi.progress.PerformInBackgroundOption.DEAF;

@UtilityClass
public final class Progress {

    public static void background(Project project, DatabaseContext context, boolean cancellable, String title, String text, ProgressRunnable runnable) {
        if (Checks.isNotValid(project)) return;
        title = Titles.suffixed(title, context);

        ThreadInfo invoker = ThreadInfo.copy();
        schedule(new Backgroundable(project, title, cancellable, ALWAYS_BACKGROUND) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                execute(indicator, ThreadProperty.PROGRESS, project, invoker, text, runnable);
            }
        });
    }


    public static void prompt(Project project, DatabaseContext context, boolean cancellable, String title, String text, ProgressRunnable runnable) {
        if (Checks.isNotValid(project)) return;
        title = Titles.suffixed(title, context);

        ThreadInfo invoker = ThreadInfo.copy();
        schedule(new Task.Backgroundable(project, title, cancellable, DEAF) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                execute(indicator, ThreadProperty.PROGRESS, project, invoker, text, runnable);
            }

            @Override
            public boolean shouldStartInBackground() {
                return false;
            }

            @Override
            public boolean isConditionalModal() {
                // TODO return true;
                return false;
            }
        });
    }


    public static void modal(Project project, DatabaseContext context, boolean cancellable, String title, String text, ProgressRunnable runnable) {
        if (Checks.isNotValid(project)) return;
        title = Titles.suffixed(title, context);

        ThreadInfo invoker = ThreadInfo.copy();
        schedule(new Task.Modal(project, title, cancellable) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                execute(indicator, ThreadProperty.MODAL, project, invoker, text, runnable);
            }
        });
    }

    private static void execute(ProgressIndicator indicator, ThreadProperty threadProperty, Project project, ThreadInfo invoker, String text, ProgressRunnable runnable) {
        ThreadMonitor.surround(project, invoker, threadProperty, () -> Failsafe.guarded(() -> {
            indicator.setText(text);
            runnable.run(indicator);
        }));
    }

    private static void schedule(Task task) {
        if (!Checks.allValid(task, task.getProject())) return;

        ProgressManager progressManager = ProgressManager.getInstance();
        Dispatch.run(() -> progressManager.run(task));
    }

    public static double progressOf(int is, int should) {
        return ((double) is) / should;
    }
}
