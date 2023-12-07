package com.dbn.common.load;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public final class ProgressMonitor {

    @Nullable
    public static ProgressIndicator getProgressIndicator() {
        Application application = ApplicationManager.getApplication();
        if (application == null) return null;

        return ProgressManager.getInstance().getProgressIndicator();
    }

    private static ProgressIndicator progress() {
        ProgressIndicator progress = getProgressIndicator();
        return progress == null ? DevNullProgressIndicator.INSTANCE : progress;
    }

    public static void checkCancelled() {
        ProgressManager.checkCanceled();
    }

    public static boolean isProgressCancelled() {
        return progress().isCanceled();
    }

    public static boolean isProgressThread() {
        return getProgressIndicator() != null;
    }

    public static void setProgressIndeterminate(boolean indeterminate) {
        progress().setIndeterminate(indeterminate);
    }

    public static void setProgressFraction(double fraction) {
        progress().setFraction(fraction);
    }

    public static void setProgressText(String text) {
        progress().setText(text);
    }

    public static void setProgressDetail(String subtext) {
        progress().setText2(subtext);
    }

    public static boolean isModal() {
        return progress().isModal();
    }

    public static boolean isProgress() {
        return progress() != DevNullProgressIndicator.INSTANCE;
    }
}
