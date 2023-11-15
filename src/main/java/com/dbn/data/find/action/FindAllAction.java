package com.dbn.data.find.action;

import com.dbn.data.find.DataSearchComponent;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class FindAllAction extends DataSearchHeaderAction implements DumbAware {
    public FindAllAction(DataSearchComponent searchComponent) {
        super(searchComponent);
        getTemplatePresentation().setIcon(AllIcons.ToolbarDecorator.Export);
        getTemplatePresentation().setDescription("Export matches to Find tool window");
        getTemplatePresentation().setText("Find All");
        registerCustomShortcutSet(ActionManager.getInstance().getAction(IdeActions.ACTION_FIND_USAGES).getShortcutSet(),
                searchComponent.getSearchField());
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
/*
        Editor editor = getEditorSearchComponent().getEditor();
        Project project = editor.getProject();
        if (project != null) {
            e.getPresentation().setEnabled(getEditorSearchComponent().hasMatches() &&
                    PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument()) != null);
        }
*/
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
/*
        Editor editor = getEditorSearchComponent().getEditor();
        Project project = editor.getProject();
        if (project != null && !project.isDisposed()) {
            final FindModel model = FindManager.getInstance(project).getFindInFileModel();
            final FindModel realModel = (FindModel) model.clone();
            String text = getEditorSearchComponent().getTextInField();
            if (StringUtil.isEmptyOrSpaces(text)) return;
            realModel.setStringToFind(text);
            FindUtil.findAllAndShow(project, editor, realModel);
        }
*/
    }
}
