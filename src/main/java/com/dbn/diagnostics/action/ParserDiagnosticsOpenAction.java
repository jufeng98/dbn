package com.dbn.diagnostics.action;

import com.dbn.common.action.ProjectAction;
import com.dbn.diagnostics.Diagnostics;
import com.dbn.diagnostics.ParserDiagnosticsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class ParserDiagnosticsOpenAction extends ProjectAction {
    public ParserDiagnosticsOpenAction() {
        super("Parser Diagnostics...");
    }

    @Override
    protected void actionPerformed(@NotNull AnActionEvent e, @NotNull Project project) {
        ParserDiagnosticsManager diagnosticsManager = ParserDiagnosticsManager.get(project);
        diagnosticsManager.openParserDiagnostics(null);
    }

    @Override
    protected void update(@NotNull AnActionEvent e, @NotNull Project project) {
        e.getPresentation().setVisible(Diagnostics.isDeveloperMode());
    }


}
