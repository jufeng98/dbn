package com.dbn.database.interfaces;

import com.dbn.common.database.AuthenticationInfo;
import com.dbn.common.database.DatabaseInfo;
import com.dbn.connection.SchemaId;
import com.dbn.database.CmdLineExecutionInput;
import com.dbn.database.common.execution.MethodExecutionProcessor;
import com.dbn.execution.script.CmdLineInterface;
import com.dbn.object.DBMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseExecutionInterface {
    MethodExecutionProcessor createExecutionProcessor(DBMethod method);
    MethodExecutionProcessor createDebugExecutionProcessor(DBMethod method);

    CmdLineExecutionInput createScriptExecutionInput(
            @NotNull CmdLineInterface cmdLineInterface,
            @NotNull String filePath,
            String content,
            @Nullable SchemaId schemaId,
            @NotNull DatabaseInfo databaseInfo,
            @NotNull AuthenticationInfo authenticationInfo);
}
