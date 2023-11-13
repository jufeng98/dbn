package com.dbn.generator;

import com.dbn.common.component.ProjectComponentBase;
import com.dbn.object.DBTable;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dbn.common.component.Components.projectService;

public class StatementGenerationManager extends ProjectComponentBase {

    public static final String COMPONENT_NAME = "DBNavigator.Project.StatementGenerationManager";

    private StatementGenerationManager(Project project) {
        super(project, COMPONENT_NAME);
    }

    public static StatementGenerationManager getInstance(@NotNull Project project) {
        return projectService(project, StatementGenerationManager.class);
    }

    public StatementGeneratorResult generateSelectStatement(List<DBObject> objects, boolean enforceAliasUsage) {
        SelectStatementGenerator generator = new SelectStatementGenerator(objects, enforceAliasUsage);
        return generator.generateStatement(getProject());
    }

    public StatementGeneratorResult generateInsert(DBTable table) {
        InsertStatementGenerator generator = new InsertStatementGenerator(table);
        return generator.generateStatement(getProject());
    }
}
