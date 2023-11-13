package com.dbn.editor.data.statusbar;

import com.dbn.common.compatibility.CompatibilityUtil;
import com.dbn.common.component.ProjectComponentBase;
import com.dbn.common.event.ProjectEvents;
import com.dbn.common.icon.Icons;
import com.dbn.common.listener.DBNFileEditorManagerListener;
import com.dbn.common.thread.Dispatch;
import com.dbn.common.ui.util.UserInterface;
import com.dbn.common.util.MathResult;
import com.dbn.common.util.Safe;
import com.dbn.editor.data.DatasetEditor;
import com.dbn.editor.data.ui.table.DatasetEditorTable;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static com.dbn.common.component.Components.projectService;

public class DatasetEditorStatusBarWidget extends ProjectComponentBase implements CustomStatusBarWidget {
    private static final String WIDGET_ID = DatasetEditorStatusBarWidget.class.getName();
    public static final String COMPONENT_NAME = "DBNavigator.Project.DatasetEditorStatusBarWidget";

    private final JLabel textLabel;
    private final Alarm updateAlarm = Dispatch.alarm(this);
    private final JPanel component = new JPanel(new BorderLayout());

    DatasetEditorStatusBarWidget(@NotNull Project project) {
        super(project, COMPONENT_NAME);
        textLabel = new JLabel();
        component.add(textLabel, BorderLayout.WEST);

        ProjectEvents.subscribe(project, this, FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorManagerListener());

        Dispatch.run(() -> registerStatusBarWidget());
    }

    public static DatasetEditorStatusBarWidget getInstance(@NotNull Project project) {
        return projectService(project, DatasetEditorStatusBarWidget.class);
    }

    FileEditorManagerListener fileEditorManagerListener() {
        return new DBNFileEditorManagerListener() {
            @Override
            public void whenSelectionChanged(@NotNull FileEditorManagerEvent event) {
                update();
            }

            @Override
            public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                update();
            }

            @Override
            public void whenFileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                update();
            }
        };
    }


    private void registerStatusBarWidget() {
        Project project = getProject();
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        statusBar.addWidget(this, this);
    }


    @NotNull
    @Override
    public String ID() {
        return WIDGET_ID;
    }

    @Nullable
    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType platformType) {
        return null;
    }

    @Nullable
    private DatasetEditor getSelectedEditor() {
        Project project = getProject();
        FileEditor selectedEditor = CompatibilityUtil.getSelectedEditor(project);
        if (selectedEditor instanceof DatasetEditor) {
            return (DatasetEditor) selectedEditor;
        }
        return null;
    }

    @Nullable
    private DatasetEditorTable getEditorTable() {
        DatasetEditor selectedEditor = getSelectedEditor();
        return selectedEditor == null ? null : selectedEditor.getEditorTable();
    }

    public void update() {
        Dispatch.alarmRequest(updateAlarm, 100, true, () -> {
            DatasetEditorTable editorTable = getEditorTable();
            MathResult mathResult = Safe.call(editorTable, table -> table.getSelectionMath());

            if (mathResult == null) {
                textLabel.setText("");
                textLabel.setIcon(null);
            } else {
                textLabel.setText(" " +
                        "Sum " +  mathResult.getSum() + "   " +
                        "Count " + mathResult.getCount() + "   " +
                        "Average " + mathResult.getAverage());
                textLabel.setIcon(Icons.COMMON_DATA_GRID);
            }
            UserInterface.repaint(getComponent());
        });
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {

    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    public void dispose() {
        disposeInner();
    }
}
