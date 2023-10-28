package com.dci.intellij.dbn.language.sql.dialect;

import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.language.common.DBLanguageDialect;
import com.dci.intellij.dbn.language.common.DBLanguageDialectIdentifier;
import com.dci.intellij.dbn.language.common.element.ChameleonElementType;
import com.dci.intellij.dbn.language.sql.SQLFileElementType;
import com.dci.intellij.dbn.language.sql.SQLLanguage;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SQLLanguageDialect extends DBLanguageDialect {
    private final Latent<ChameleonElementType> psqlChameleonElementType = Latent.basic(() -> createPsqlChameleonElementType());

    @Nullable
    private ChameleonElementType createPsqlChameleonElementType() {
        DBLanguageDialectIdentifier chameleonDialectIdentifier = getChameleonDialectIdentifier();
        if (chameleonDialectIdentifier == null) return null;

        DBLanguageDialect plsqlDialect = DBLanguageDialect.get(chameleonDialectIdentifier);
        return new ChameleonElementType(plsqlDialect, SQLLanguageDialect.this);
    }

    public SQLLanguageDialect(@NonNls @NotNull DBLanguageDialectIdentifier identifier) {
        super(identifier, SQLLanguage.INSTANCE);
    }

    @Override
    public IFileElementType createFileElementType() {
        return new SQLFileElementType(this);
    }

    @Override
    public final ChameleonElementType getChameleonTokenType(DBLanguageDialectIdentifier dialectIdentifier) {
        if (dialectIdentifier == getChameleonDialectIdentifier()) {
            return psqlChameleonElementType.get();
        }
        return super.getChameleonTokenType(dialectIdentifier);
    }

    @Nullable
    protected DBLanguageDialectIdentifier getChameleonDialectIdentifier() {
        return null;
    }

}
