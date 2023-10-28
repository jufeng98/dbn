package com.dci.intellij.dbn.language.common;

import com.dci.intellij.dbn.common.action.UserDataKeys;
import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.language.common.element.ChameleonElementType;
import com.dci.intellij.dbn.language.common.element.TokenPairTemplate;
import com.dci.intellij.dbn.language.psql.PSQLLanguage;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IFileElementType;
import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.dci.intellij.dbn.common.dispose.Checks.isNotValid;

@Getter
public abstract class DBLanguageDialect extends Language implements DBFileElementTypeProvider {

    private static final Map<DBLanguageDialectIdentifier, DBLanguageDialect> REGISTRY = new EnumMap<>(DBLanguageDialectIdentifier.class);
    private static final TokenPairTemplate[] TOKEN_PAIR_TEMPLATES = new TokenPairTemplate[] {TokenPairTemplate.PARENTHESES};

    private final DBLanguageDialectIdentifier identifier;

    private final Latent<DBLanguageSyntaxHighlighter> syntaxHighlighter = Latent.basic(() -> createSyntaxHighlighter());
    private final Latent<DBLanguageParserDefinition> parserDefinition = Latent.basic(() -> createParserDefinition());
    private final Latent<IFileElementType> fileElementType = Latent.basic(() -> createFileElementType());

    private Set<ChameleonTokenType> chameleonTokens;

    public DBLanguageDialect(@NonNls @NotNull DBLanguageDialectIdentifier identifier, @NotNull DBLanguage baseLanguage) {
        super(baseLanguage, identifier.getValue());
        this.identifier = identifier;
        REGISTRY.put(identifier, this);
    }

    protected abstract Set<ChameleonTokenType> createChameleonTokenTypes();
    protected abstract DBLanguageSyntaxHighlighter createSyntaxHighlighter() ;
    protected abstract DBLanguageParserDefinition createParserDefinition();
    protected abstract IFileElementType createFileElementType();

    public ChameleonElementType getChameleonTokenType(DBLanguageDialectIdentifier dialectIdentifier) {
        throw new IllegalArgumentException("Language " + getID() + " does not support chameleons of type " + dialectIdentifier.getValue() );
    }

    public boolean isReservedWord(String identifier) {
        return getParserTokenTypes().isReservedWord(identifier);
    }

    @Override
    @NotNull
    public DBLanguage getBaseLanguage() {
        return Failsafe.nn((DBLanguage) super.getBaseLanguage());
    }

    public SharedTokenTypeBundle getSharedTokenTypes() {
        return getBaseLanguage().getSharedTokenTypes();
    }

    public DBLanguageSyntaxHighlighter getSyntaxHighlighter() {
        return syntaxHighlighter.get();
    }

    @NotNull
    public DBLanguageParserDefinition getParserDefinition() {
        return parserDefinition.get();
    }

    @Override
    public IFileElementType getFileElementType() {
        return fileElementType.get();
    }

    public TokenTypeBundle getParserTokenTypes() {
        return getParserDefinition().getParser().getTokenTypes();
    }

    public TokenTypeBundle getHighlighterTokenTypes() {
        return getSyntaxHighlighter().getTokenTypes();
    }

    public TokenType getInjectedLanguageToken(DBLanguageDialectIdentifier dialectIdentifier) {
        if (chameleonTokens == null) {
            chameleonTokens = createChameleonTokenTypes();
            if (chameleonTokens == null) chameleonTokens = new HashSet<>();
        }
        for (ChameleonTokenType chameleonToken : chameleonTokens) {
            if (chameleonToken.getInjectedLanguage().identifier == dialectIdentifier) {
                return chameleonToken;
            }
        }
        return null;
    }

    public TokenPairTemplate[] getTokenPairTemplates() {
        return TOKEN_PAIR_TEMPLATES;
    }


    public static DBLanguageDialect get(DBLanguageDialectIdentifier identifier) {
         // make sure all dialects are loaded before doing this lookup
         SQLLanguage.INSTANCE.getAvailableLanguageDialects();
         PSQLLanguage.INSTANCE.getAvailableLanguageDialects();
         return REGISTRY.get(identifier);
    }

    @Nullable
    public static DBLanguageDialect get(DBLanguage language, @Nullable ConnectionHandler connection) {
        if (connection == null) return null;
        return connection.getLanguageDialect(language);

    }

    @Nullable
    public static DBLanguageDialect get(@NotNull DBLanguage language, @Nullable VirtualFile file, @Nullable Project project) {
        if (isNotValid(project)) return null;
        if (isNotValid(file)) return null;

        DBLanguageDialect languageDialect = file.getUserData(UserDataKeys.LANGUAGE_DIALECT);
        if (languageDialect != null) return languageDialect;

        FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
        ConnectionHandler connection = contextManager.getConnection(file);
        if (connection == null) return null;

        return connection.getLanguageDialect(language);
    }
}
