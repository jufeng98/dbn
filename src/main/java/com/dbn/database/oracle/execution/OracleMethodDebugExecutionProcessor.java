package com.dbn.database.oracle.execution;

import com.dbn.execution.common.options.ExecutionEngineSettings;
import com.dbn.execution.method.options.MethodExecutionSettings;
import com.dbn.object.DBMethod;
import com.intellij.openapi.project.Project;

public class OracleMethodDebugExecutionProcessor extends OracleMethodExecutionProcessor {
    public OracleMethodDebugExecutionProcessor(DBMethod method) {
        super(method);
    }

    @Override
    protected void preHookExecutionCommand(StringBuilder buffer) {
        super.preHookExecutionCommand(buffer);
        DBMethod method = getMethod();
        Project project = method.getProject();
        MethodExecutionSettings methodExecutionSettings = ExecutionEngineSettings.getInstance(project).getMethodExecutionSettings();
        int debugExecutionTimeout = methodExecutionSettings.getDebugExecutionTimeout();
        if (debugExecutionTimeout > 0) {
            buffer.append("\n    v_timeout := SYS.DBMS_DEBUG.set_timeout(").append(debugExecutionTimeout).append(");\n");
        }
    }

    @Override
    protected void postHookExecutionCommand(StringBuilder buffer) {
        buffer.append("\n");
        buffer.append("    SYS.DBMS_DEBUG.debug_off();\n");
        buffer.append("exception\n");
        buffer.append("    when others then\n");
        buffer.append("        SYS.DBMS_DEBUG.debug_off();\n");
        buffer.append("        raise;\n");
    }

}