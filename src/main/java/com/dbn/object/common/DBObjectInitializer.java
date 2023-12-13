package com.dbn.object.common;

import com.dbn.common.thread.Background;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.ConnectionRef;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.object.type.DBObjectType;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.dbn.common.dispose.Checks.isValid;

public class DBObjectInitializer {
    private final ConnectionRef connection;
    private final Set<String> current = ContainerUtil.newConcurrentSet();

    public DBObjectInitializer(@NotNull ConnectionHandler connection) {
        this.connection = connection.ref();
    }

    public Project getProject() {
        return connection.ensure().getProject();
    }

    public void initObject(DBObjectRef ref) {
        DBObject object = ref.value();
        if (isValid(object)) return;

        DBObjectRef parentRef = ref.getParentRef();
        if (parentRef == null) {
            initRootObject(ref);
        } else  {
            initChildObject(ref, parentRef);
        }
    }

    private void initRootObject(DBObjectRef ref) {
        Project project = getProject();

        String identifier = identifier(ref, null);
        if (current.contains(identifier)) return;

        synchronized (this) {
            if (current.contains(identifier)) return;

            current.add(identifier);
            Background.run(project, () -> {
                try {
                    ref.get();
                } finally {
                    current.remove(identifier);
                }

            });
        }
    }

    private void initChildObject(DBObjectRef ref, DBObjectRef parentRef) {
        Project project = getProject();

        DBObjectType objectType = ref.getObjectType();
        String identifier = identifier(parentRef, objectType);
        if (current.contains(identifier)) return;

        synchronized (this) {
            if (current.contains(identifier)) return;

            current.add(identifier);
            Background.run(project, () -> {
                try {
                    DBObject parent = parentRef.get();
                    if (parent == null) return;

                    parent.getChildObjects(objectType);
                } finally {
                    current.remove(identifier);
                }
            });
        }
    }


    private String identifier(DBObjectRef ref, DBObjectType objectType) {
        return ref.getPath() + "#" + objectType;
    }

}
