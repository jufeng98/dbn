package com.dci.intellij.dbn.vfs.file;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.SlowOps;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.session.DatabaseSession;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.dci.intellij.dbn.object.DBDataset;
import com.dci.intellij.dbn.object.lookup.DBObjectRef;
import com.dci.intellij.dbn.vfs.DBParseableVirtualFile;
import com.dci.intellij.dbn.vfs.DBVirtualFileBase;
import com.dci.intellij.dbn.vfs.DatabaseFileViewProvider;
import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;

import static com.dci.intellij.dbn.common.action.UserDataKeys.LANGUAGE_DIALECT;

@Getter
@Setter
public class DBDatasetFilterVirtualFile extends DBVirtualFileBase implements DBParseableVirtualFile {
    private final DBObjectRef<DBDataset> dataset;
    private CharSequence content;

    public DBDatasetFilterVirtualFile(DBDataset dataset, String content) {
        super(dataset.getProject(), dataset.getName());
        this.dataset = DBObjectRef.of(dataset);
        this.content = content;
        ConnectionHandler connection = Failsafe.nn(getConnection());
        setCharset(connection.getSettings().getDetailSettings().getCharset());
        putUserData(PARSE_ROOT_ID_KEY, "subquery");
        putUserData(LANGUAGE_DIALECT, DBLanguageDialect.get(SQLLanguage.INSTANCE, connection));
    }

    @Override
    public PsiFile initializePsiFile(DatabaseFileViewProvider fileViewProvider, Language language) {
        ConnectionHandler connection = Failsafe.nn(getConnection());
        DBLanguageDialect languageDialect = connection.resolveLanguageDialect(language);
        return languageDialect == null ? null : fileViewProvider.initializePsiFile(languageDialect);
    }

    @Override
    public boolean isValid() {
        return SlowOps.isValid(dataset);
    }

    public DBDataset getDataset() {
        return DBObjectRef.get(dataset);
    }

    @Override
    public Icon getIcon() {
        return Icons.DBO_TABLE;
    }


    @NotNull
    @Override
    public ConnectionId getConnectionId() {
        return dataset.getConnectionId();
    }

    @Override
    @NotNull
    public ConnectionHandler getConnection() {
        return dataset.ensureConnection();
    }

    @Nullable
    @Override
    public SchemaId getSchemaId() {
        DBDataset dataset = getDataset();
        return dataset == null ? null : dataset.getSchemaId();
    }

    @Nullable
    @Override
    public DatabaseSession getSession() {
        return getConnection().getSessionBundle().getMainSession();
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public VirtualFile getParent() {
        return null;
    }

    @Override
    @NotNull
    public OutputStream getOutputStream(Object requestor, long modificationStamp, long timeStamp) throws IOException {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                setContent(this.toString());

                setTimeStamp(timeStamp);
                setModificationStamp(modificationStamp);
            }
        };
    }

    @Override
    @NotNull
    public byte[] contentsToByteArray() throws IOException {
        Charset charset = getCharset();
        return content.toString().getBytes(charset);
    }

    @Override
    public long getLength() {
        return content.length();
    }

    @Override
    public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable) {
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

}
