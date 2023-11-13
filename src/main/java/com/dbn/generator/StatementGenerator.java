package com.dbn.generator;

import com.intellij.openapi.project.Project;

public abstract class StatementGenerator {

    public abstract StatementGeneratorResult generateStatement(Project project);
}
