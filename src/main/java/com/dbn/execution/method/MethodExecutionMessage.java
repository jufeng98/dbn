package com.dbn.execution.method;

import com.dbn.common.message.MessageType;
import com.dbn.connection.ConnectionId;
import com.dbn.database.common.execution.MethodExecutionProcessor;
import com.dbn.editor.DBContentType;
import com.dbn.execution.common.message.ConsoleMessage;
import com.dbn.object.DBMethod;
import com.dbn.vfs.file.DBContentVirtualFile;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class MethodExecutionMessage extends ConsoleMessage {
    private MethodExecutionProcessor executionProcessor;
    private DBEditableObjectVirtualFile databaseFile;
    private DBContentVirtualFile contentFile;
    private DBContentType contentType;
    private ConnectionId connectionId;

    public MethodExecutionMessage(MethodExecutionProcessor executionProcessor, String message, MessageType messageType) {
        super(messageType, message);
        this.executionProcessor = executionProcessor;
        this.connectionId = executionProcessor.getMethod().getConnectionId();
    }

    public DBEditableObjectVirtualFile getDatabaseFile() {
        if (databaseFile == null) {
            DBMethod method = executionProcessor.getMethod();
            databaseFile = method.getEditableVirtualFile();
        }
        return databaseFile;
    }

    @Nullable
    public DBContentVirtualFile getContentFile() {
        if (contentFile == null) {
            DBEditableObjectVirtualFile databaseFile = getDatabaseFile();
            contentFile = databaseFile.getContentFile(contentType);
        }
        return contentFile;
    }
}
