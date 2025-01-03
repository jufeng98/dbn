package com.dbn.vfs.file;

import com.dbn.code.common.style.DBLCodeStyleManager;
import com.dbn.code.common.style.options.CodeStyleCaseSettings;
import com.dbn.common.icon.Icons;
import com.dbn.common.util.Strings;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.SchemaId;
import com.dbn.connection.SessionId;
import com.dbn.connection.mapping.FileConnectionContext;
import com.dbn.connection.mapping.FileConnectionContextImpl;
import com.dbn.connection.mapping.FileConnectionContextProvider;
import com.dbn.connection.session.DatabaseSession;
import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dbn.editor.code.content.SourceCodeContent;
import com.dbn.language.common.DBLanguage;
import com.dbn.language.common.DBLanguageDialect;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLFileType;
import com.dbn.object.DBConsole;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.vfs.DBConsoleType;
import com.dbn.vfs.DBParseableVirtualFile;
import com.dbn.vfs.DatabaseFileViewProvider;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.DocumentEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

@Getter
public class DBConsoleVirtualFile extends DBObjectVirtualFile<DBConsole> implements DocumentListener, DBParseableVirtualFile, Comparable<DBConsoleVirtualFile>, FileConnectionContextProvider {
    private final SourceCodeContent content = new SourceCodeContent();
    private final FileConnectionContext connectionContext;

    public DBConsoleVirtualFile(@NotNull DBConsole console) {
        super(console.getProject(), DBObjectRef.of(console));

        ConnectionHandler connection = console.getConnection();
        SessionId sessionId = connection.getSessionBundle().getMainSession().getId();
        connectionContext = createConnectionContext(this, sessionId);

        setCharset(connection.getSettings().getDetailSettings().getCharset());
    }

    public void setText(String text) {
        if (getObject().getConsoleType() == DBConsoleType.DEBUG && Strings.isEmpty(text)) {
            ConnectionHandler connection = getConnection();
            Project project = connection.getProject();

            DatabaseDebuggerInterface debuggerInterface = connection.getDebuggerInterface();
            CodeStyleCaseSettings styleCaseSettings = DBLCodeStyleManager.getInstance(project).getCodeStyleCaseSettings(PSQLLanguage.INSTANCE);
            text = debuggerInterface.getDebugConsoleTemplate(styleCaseSettings);
        }
        content.importContent(text);
    }

    @Override
    public PsiFile initializePsiFile(DatabaseFileViewProvider fileViewProvider, DBLanguage<?> language) {
        ConnectionHandler connection = getConnection();
        DBLanguageDialect languageDialect = connection.resolveLanguageDialect(language);
        return languageDialect == null ? null : fileViewProvider.initializePsiFile(languageDialect);
    }

    public void setName(String name) {
        super.setName(name);
        this.path = null;
        this.url = null;
   }

    @NotNull
    public DBConsole getConsole() {
        return getObject();
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return switch (getType()) {
            case STANDARD -> Icons.FILE_SQL_CONSOLE;
            case DEBUG -> Icons.FILE_SQL_DEBUG_CONSOLE;
            default -> null;
        };
    }
    public void setDatabaseSchema(SchemaId schemaId) {
        connectionContext.setSchemaId(schemaId);
    }

    public void setDatabaseSchemaName(String schemaName) {
        if (Strings.isEmpty(schemaName)) {
            setDatabaseSchema(null);
        } else {
            setDatabaseSchema(SchemaId.get(schemaName));
        }
    }

    public String getDatabaseSchemaName() {
        return connectionContext.getSchemaName();
    }

    @SuppressWarnings("unused")
    public void setDatabaseSessionId(SessionId sessionId) {
        connectionContext.setSessionId(sessionId);
    }

    @Override
    public DatabaseSession getSession() {
        return connectionContext.getSession();
    }

    public void setDatabaseSession(DatabaseSession databaseSession) {
        this.connectionContext.setSessionId(databaseSession == null ? SessionId.MAIN : databaseSession.getId());
    }

    @Override
    public boolean isValid() {
        return super.isValid() /*&& connection.isValid()*/;
    }

    @Override
    @Nullable
    public SchemaId getSchemaId() {
        return connectionContext.getSchemaId();
    }

    public DBConsoleType getType() {
        return getObject().getConsoleType();
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public boolean isDefault() {
        return Objects.equals(getName(), getConnection().getName());
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SQLFileType.INSTANCE;
    }

    @Override
    @NotNull
    public OutputStream getOutputStream(Object requestor, long modificationStamp, long timeStamp) {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                content.setText(toString());

                setTimeStamp(timeStamp);
                setModificationStamp(modificationStamp);

            }
        };
    }

    @Override
    public byte @NotNull [] contentsToByteArray() throws IOException {
        Charset charset = getCharset();
        return content.getBytes(charset);
    }

    @Override
    public long getLength() {
        return content.length();
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(contentsToByteArray());
    }

    @Override
    public String getExtension() {
        return "sql";
    }

    @Override
    public int compareTo(@NotNull DBConsoleVirtualFile o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public void documentChanged(DocumentEvent event) {
        Document document = event.getDocument();
        content.setText(document.getCharsSequence());
        if (document instanceof DocumentEx documentEx) {
            List<RangeMarker> blocks = documentEx.getGuardedBlocks();
            if (!blocks.isEmpty()) {
                content.getOffsets().setGuardedBlocks(blocks);
            }
        }
    }

    private static FileConnectionContext createConnectionContext(
            DBConsoleVirtualFile consoleFile,
            SessionId sessionId) {
        return new FileConnectionContextImpl(consoleFile.getUrl(), consoleFile.getConnectionId(), sessionId, null) {
            @Override
            public void setFileUrl(String fileUrl) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean setConnectionId(ConnectionId connectionId) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
