package com.dbn.language.common;

import com.dbn.code.common.style.options.DBLCodeStyleSettings;
import com.dbn.common.latent.Latent;
import com.dbn.common.util.Unsafe;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class DBLanguage<D extends DBLanguageDialect> extends Language implements DBFileElementTypeProvider {

    private final transient Latent<D[]> languageDialects = Latent.basic(DBLanguage.this::createLanguageDialects);
    private final transient Latent<IFileElementType> fileElementType = Latent.basic(DBLanguage.this::createFileElementType);
    private final transient Latent<SharedTokenTypeBundle> sharedTokenTypes = Latent.basic(DBLanguage.this::createSharedTokenTypes);

    protected DBLanguage(final @NonNls String id, final @NonNls String... mimeTypes){
        super(id, mimeTypes);
    }

    @Override
    public final IFileElementType getFileElementType() {
        return fileElementType.get();
    }

    protected abstract D[] createLanguageDialects();

    protected abstract IFileElementType createFileElementType();

    private SharedTokenTypeBundle createSharedTokenTypes() {
        return new SharedTokenTypeBundle(this);
    }

    public SharedTokenTypeBundle getSharedTokenTypes() {
        return sharedTokenTypes.get();
    }

    public abstract D getMainLanguageDialect();

    public D getLanguageDialect(Project project, VirtualFile virtualFile) {
        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        ConnectionHandler connection = contextManager.getConnection(virtualFile);
        if (connection != null) {
            return Unsafe.cast(connection.getLanguageDialect(this));
        }
        return getMainLanguageDialect();
    }

    @NotNull
    public D[] getAvailableLanguageDialects() {
        return languageDialects.get();
    }

    public D getLanguageDialect(DBLanguageDialectIdentifier id) {
        for (D languageDialect: getAvailableLanguageDialects()) {
            if (Objects.equals(languageDialect.getID(), id.getValue())) {
                return languageDialect;
            }
        }
        return null;
    }

    public abstract DBLCodeStyleSettings codeStyleSettings(@Nullable Project project);

    public DBLanguageParserDefinition getParserDefinition(ConnectionHandler connection) {
        return connection.getLanguageDialect(this).getParserDefinition();
    }

    public static DBLanguage getLanguage(String identifier) {
        if (identifier.equalsIgnoreCase("SQL")) return SQLLanguage.INSTANCE;
        if (identifier.equalsIgnoreCase("PSQL")) return PSQLLanguage.INSTANCE;
        return null;
    }
}
