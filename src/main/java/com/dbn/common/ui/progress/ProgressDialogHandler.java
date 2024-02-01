package com.dbn.common.ui.progress;

import com.dbn.common.project.ProjectRef;
import com.dbn.common.util.Dialogs;
import com.dbn.common.util.Timers;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Getter
public class ProgressDialogHandler {
    private final static Set<ProgressDialog> progressDialogs = new HashSet<>();

    private final ProjectRef project;
    private final String title;
    private final String text;
    private ProgressDialog progressDialog;
    private ProgressIndicator progressIndicator;

    public ProgressDialogHandler(Project project, String title, String text) {
        this.project = ProjectRef.of(project);
        this.title = title;
        this.text = text;
    }

    public Project getProject() {
        return project.ensure();
    }

    public void init(@NotNull ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public void trigger() {
        // delay the creation of the dialog 1 second to reduce number of prompts if background process finishes in acceptable time
        Timers.executeLater("ProgressDialogPrompt", 300, MILLISECONDS, () -> {
            if (finished()) return;
            openDialog();
        });
    }

    private void openDialog() {
        Dialogs.show(() -> {
            if (finished()) return null;
            progressDialog = new ProgressDialog(this);
            progressDialogs.add(progressDialog);
            return progressDialog;
        });
        if (finished()) release();
    }

    private boolean finished() {
        if (progressIndicator.isCanceled()) return true;
        if (!progressIndicator.isRunning()) return true;
        return false;
    }

    public void cancel() {
        progressIndicator.cancel();
    }

    public void release() {
        Dialogs.close(progressDialog, DialogWrapper.OK_EXIT_CODE);
    }

    public static void closeProgressDialogs() {
        Iterator<ProgressDialog> dialogs = progressDialogs.iterator();
        while (dialogs.hasNext()) {
            ProgressDialog progressDialog = dialogs.next();
            progressDialog.close(DialogWrapper.OK_EXIT_CODE);
            dialogs.remove();
        }
    }
}
