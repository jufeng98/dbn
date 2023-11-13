package com.dbn.vfs.file;

import com.dbn.common.navigation.NavigationInstructions;
import com.dbn.common.util.Unsafe;
import com.dbn.editor.DatabaseEditorStateManager;
import com.dbn.editor.EditorProviderId;
import com.dbn.object.common.DBObject;
import com.dbn.object.lookup.DBObjectRef;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class DBFileOpenHandle {
    private static final Set<DBObjectRef<?>> REGISTRY = ConcurrentHashMap.newKeySet();

    private final DBObjectRef<?> object;
    private EditorProviderId editorProviderId;
    private NavigationInstructions browserInstructions = NavigationInstructions.NONE;
    private NavigationInstructions editorInstructions = NavigationInstructions.NONE;

    private DBFileOpenHandle(@NotNull DBObject object) {
        this.object = DBObjectRef.of(object);
    }

    public static <T extends DBObject> DBFileOpenHandle create(@NotNull T object) {
        return new DBFileOpenHandle(object);
    }

    public DBFileOpenHandle withBrowserInstructions(@NotNull NavigationInstructions browserInstructions) {
        this.browserInstructions = browserInstructions;
        return this;
    }

    public DBFileOpenHandle withEditorInstructions(@NotNull NavigationInstructions editorInstructions) {
        this.editorInstructions = editorInstructions;
        return this;
    }

    public DBFileOpenHandle withEditorProviderId(EditorProviderId editorProviderId) {
        this.editorProviderId = editorProviderId;
        return this;
    }

    public EditorProviderId getEditorProviderId() {
        if (editorProviderId == null) {
            DatabaseEditorStateManager editorStateManager = DatabaseEditorStateManager.getInstance(getObject().getProject());
            editorProviderId = editorStateManager.getEditorProvider(object.getObjectType());
        }
        return editorProviderId;
    }

    @NotNull
    public <T extends DBObject> T getObject() {
        return Unsafe.cast(DBObjectRef.ensure(object));
    }

    public static boolean isFileOpening(@NotNull DBObject object) {
        return REGISTRY.contains(object.ref());
    }

    public void init() {
        REGISTRY.add(object);
    }

    public void release() {
        REGISTRY.remove(object);
    }
}
