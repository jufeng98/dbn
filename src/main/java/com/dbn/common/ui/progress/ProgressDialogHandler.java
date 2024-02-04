package com.dbn.common.ui.progress;

import com.dbn.common.project.ProjectRef;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.util.Timers;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.JBDimension;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Getter
public class ProgressDialogHandler {
    private final static Set<JBPopup> progressDialogs = new HashSet<>();

    private final ProjectRef project;
    private final String title;
    private final String text;
    private JBPopup progressDialog;
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
            openPopup();
        });
    }

    private void openPopup() {
        Dispatch.run(true, () -> {
            if (finished()) return;

            closeProgressDialogs();

            progressDialog = createPopup();
            progressDialog.showCenteredInCurrentWindow(getProject());

            progressDialogs.add(progressDialog);
        });
        if (finished()) release();

    }

    private JBPopup createPopup() {
        ProgressDialogForm form = new ProgressDialogForm(this);
        JComponent content = form.getMainComponent();
        JComponent focus = form.getPreferredFocusedComponent();
        ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(content, focus);

        builder.setProject(getProject());
        builder.setNormalWindowLevel(true);
        builder.setMovable(true);
        builder.setResizable(true);
        builder.setTitle(title);
        builder.setCancelOnClickOutside(false);
        builder.setRequestFocus(true);
        builder.setBelongsToGlobalPopupStack(false);
        builder.setMinSize(new JBDimension(300, 100));
        builder.setLocateWithinScreenBounds(false);
        return builder.createPopup();
    }

    private boolean finished() {
        if (progressIndicator.isCanceled()) return true;
        if (!progressIndicator.isRunning()) return true;
        return false;
    }

    public void cancel() {
        progressIndicator.cancel();
        release();
    }

    public void release() {
        closePopup(progressDialog);
        progressDialog = null;
    }

    private void closePopup(JBPopup popup) {
        if (popup == null) return;

        Dispatch.run(true, () -> {
            popup.cancel();
            Disposer.dispose(popup);
        });

    }

    private static void closeProgressDialogs() {
        Iterator<JBPopup> dialogs = progressDialogs.iterator();
        while (dialogs.hasNext()) {
            JBPopup progressDialog = dialogs.next();
            progressDialog.cancel();
            Disposer.dispose(progressDialog);
            dialogs.remove();
        }
    }
}
