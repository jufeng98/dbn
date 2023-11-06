package com.dci.intellij.dbn.language.common.psi;

import com.dci.intellij.dbn.common.property.PropertyHolderBase;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.SchemaId;
import com.dci.intellij.dbn.connection.context.DatabaseContext;
import com.dci.intellij.dbn.language.common.PsiElementRef;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectPsiElement;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.dci.intellij.dbn.common.util.Commons.match;
import static com.dci.intellij.dbn.language.common.psi.PsiResolveStatus.*;

@Getter
public final class PsiResolveResult extends PropertyHolderBase.IntStore<PsiResolveStatus> {
    private ConnectionId connectionId;
    private SchemaId schemaId;
    private PsiElementRef<IdentifierPsiElement> element;
    private PsiElementRef<BasePsiElement> parent;
    private PsiElementRef<?> reference;
    private CharSequence text;
    private long lastResolveInvocation = 0;
    private int scopeTextLength;
    private int resolveAttempts = 0;
    private short signature;

    PsiResolveResult(IdentifierPsiElement element) {
        this.connectionId = element.getConnectionId();
        this.element = PsiElementRef.of(element);
        set(PsiResolveStatus.NEW, true);
    }

    @Override
    protected PsiResolveStatus[] properties() {
        return VALUES;
    }

    public void accept(IdentifierPsiElement element) {
        this.element = PsiElementRef.of(element);
    }

    public void preResolve() {
        set(RESOLVING, true);
        IdentifierPsiElement element = getElement();
        this.text = element.getUnquotedText();
        this.reference = null;
        this.parent = null;
        this.connectionId = element.getConnectionId();
        this.schemaId = element.getSchemaId();

        ConnectionHandler connection = element.getConnection();
        set(CONNECTION_VALID, connection != null && !connection.isVirtual() && connection.isValid());
        set(CONNECTION_ACTIVE, connection != null && !connection.isVirtual() && connection.canConnect());

        BasePsiElement enclosingScopePsiElement = element.getEnclosingScopeElement();
        this.scopeTextLength = enclosingScopePsiElement == null ? 0 : enclosingScopePsiElement.getTextLength();
        if (Strings.isEmpty(text)) {
            text = "";
        }
    }

    public void postResolve(boolean cancelled) {
        set(NEW, false);
        set(RESOLVING, false);

        if (cancelled) return;

        PsiElement referencedElement = PsiElementRef.get(this.reference);
        this.resolveAttempts = referencedElement == null ? resolveAttempts + 1 : 0;
        this.lastResolveInvocation = System.currentTimeMillis();

    }

    public boolean isResolving() {
        return is(RESOLVING);
    }

    public boolean isNew() {
        return is(NEW);
    }

    boolean isDirty() {
        if (isResolving()) return false;
        if (isNew()) return true;
        if (isConnectionChanged()) {
            resolveAttempts = 0;
            return true;
        }

        PsiElement referencedElement = getReference();
        if (referencedElement == null) {
            if (resolveAttempts > 0) {
                if (resolveAttempts > 10) {
                    return false;
                }
                //  2 -> 4 -> 8 -> 16 -> 32 seconds... (give up at some point)
                long delay = (long) Math.pow(2, resolveAttempts) * 1000;
                return lastResolveInvocation < System.currentTimeMillis() - delay;
            } else {
                return true;
            }
        }

        if (!referencedElement.isValid()) {
            return true;
        }

        if (elementTextChanged() || enclosingScopeChanged()) {
            resolveAttempts = 0;
            return true;
        }

        IdentifierPsiElement element = this.element.get();
        if (element != null && !element.textMatches(referencedElement.getText())) {
            return true;
        }

        BasePsiElement parent = getParent();
        if (parent != null) {
            if (!parent.isValid()) {
                return true;
            } else if (referencedElement instanceof DBObjectPsiElement) {
                DBObjectPsiElement objectPsiElement = (DBObjectPsiElement) referencedElement;
                if (!objectPsiElement.isValid()) {
                    return true;
                }
                DBObject parentObject = objectPsiElement.ensureObject().getParentObject();
                DBObject underlyingObject = parent.getUnderlyingObject();
                return parentObject != null && !parentObject.isVirtual() &&
                        underlyingObject != null && !underlyingObject.isVirtual() &&
                        !Objects.equals(parentObject, underlyingObject);
            }
        } else {
            return element != null && element.isPrecededByDot();
        }
        return false;
    }

    private boolean isConnectionChanged() {
        if (connectionChanged() || schemaChanged()) {
            return true;
        }
        ConnectionId currentConnectionId = getCurrentConnectionId();
        ConnectionHandler currentConnection = ConnectionHandler.get(currentConnectionId);
        if (currentConnection == null || currentConnection.isVirtual()) {
            return schemaId != null;
        }

        if (connectionBecameActive(currentConnection) || connectionBecameValid(currentConnection)) {
            return true;
        }

        return false;
    }


    private boolean elementTextChanged() {
        IdentifierPsiElement element = this.element.get();
        return element!= null && !element.textMatches(text);
    }

    private boolean connectionChanged() {
        ConnectionId currentConnectionId = getCurrentConnectionId();
        if (!match(currentConnectionId, connectionId)) return true;

        ConnectionId referenceConnectionId = getReferenceConnectionId();
        ConnectionId parentConnectionId = getParentConnectionId();
        if (parentConnectionId != null && !match(parentConnectionId, referenceConnectionId)) return true;

        return false;
    }

    private boolean schemaChanged() {
        SchemaId currentSchemaId = getCurrentSchemaId();
        if (!match(schemaId, currentSchemaId)) return true;

        SchemaId parentSchemaId = getParentSchemaId();
        SchemaId referenceSchemaId = getReferenceSchemaId();
        if (parentSchemaId != null && !match(parentSchemaId, referenceSchemaId)) return true;

        return false;
    }

    private boolean connectionBecameValid(ConnectionHandler connection) {
        return isNot(CONNECTION_VALID) && connection != null && !connection.isVirtual() && connection.isValid();
    }

    private boolean connectionBecameActive(ConnectionHandler connection) {
        return isNot(CONNECTION_ACTIVE) && connection!= null && !connection.isVirtual() && connection.canConnect();
    }

    private boolean enclosingScopeChanged() {
        IdentifierPsiElement element = this.element.get();
        if (element != null) {
            BasePsiElement scopePsiElement = element.getEnclosingScopeElement();
            int scopeTextLength = scopePsiElement == null ? 0 : scopePsiElement.getTextLength();
            return this.scopeTextLength != scopeTextLength;
        }
        return false;
    }

    @NotNull
    public DBObjectType getObjectType() {
        if (is(RESOLVING_OBJECT_TYPE)) return DBObjectType.UNKNOWN;
        set(RESOLVING_OBJECT_TYPE, true);

        try {
            PsiElement referencedElement = getReference();
            if (referencedElement instanceof DBObjectPsiElement) {
                DBObjectPsiElement objectPsiElement = (DBObjectPsiElement) referencedElement;
                return objectPsiElement.getObjectType();
            }
            if (referencedElement instanceof IdentifierPsiElement) {
                IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) referencedElement;
                return identifierPsiElement.getObjectType();
            }

            if (referencedElement instanceof BasePsiElement) {
                BasePsiElement basePsiElement = (BasePsiElement) referencedElement;
                DBObject object = basePsiElement.getUnderlyingObject();
                if (object != null) {
                    return object.getObjectType();
                }
            }
        } finally {
            set(RESOLVING_OBJECT_TYPE, false);
        }

        return DBObjectType.UNKNOWN;
    }

    private IdentifierPsiElement getElement() {
        return PsiElementRef.get(element);
    }

    private BasePsiElement getParent() {
        return PsiElementRef.get(parent);
    }

    public PsiElement getReference() {
        return PsiElementRef.get(reference);
    }

    public void setParent(@Nullable BasePsiElement parent) {
        this.parent = PsiElementRef.of(parent);
    }

    public void setReference(PsiElement reference) {
        this.reference = PsiElementRef.of(reference);
        this.signature++;
    }

    private ConnectionId getCurrentConnectionId() {
        IdentifierPsiElement element = getElement();
        return element == null ? null : element.getConnectionId();
    }

    private SchemaId getCurrentSchemaId() {
        IdentifierPsiElement element = getElement();
        return element == null ? null : element.getSchemaId();
    }

    private ConnectionId getParentConnectionId() {
        BasePsiElement parent = getParent();
        return parent == null ? null : parent.getConnectionId();
    }

    private SchemaId getParentSchemaId() {
        BasePsiElement parent = getParent();
        return parent == null ? null : parent.getSchemaId();
    }

    private ConnectionId getReferenceConnectionId() {
        PsiElement reference = getReference();
        if (reference instanceof DatabaseContext) {
            DatabaseContext context = (DatabaseContext) reference;
            return context.getConnectionId();
        }
        return null;
    }

    private SchemaId getReferenceSchemaId() {
        PsiElement reference = getReference();
        if (reference instanceof DatabaseContext) {
            DatabaseContext context = (DatabaseContext) reference;
            return context.getSchemaId();
        }
        return null;
    }
}
