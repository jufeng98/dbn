package com.dci.intellij.dbn.object.lookup;

import com.dci.intellij.dbn.common.Reference;
import com.dci.intellij.dbn.common.dispose.FailsafeUtil;
import com.dci.intellij.dbn.common.state.PersistentStateElement;
import com.dci.intellij.dbn.common.thread.SimpleTimeoutCall;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.connection.ConnectionCache;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionId;
import com.dci.intellij.dbn.connection.ConnectionManager;
import com.dci.intellij.dbn.connection.ConnectionProvider;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.dci.intellij.dbn.vfs.DatabaseFileSystem.PS;

public class DBObjectRef<T extends DBObject> implements Comparable, Reference<T>, PersistentStateElement<Element>, ConnectionProvider {
    protected DBObjectRef parent;
    protected DBObjectType objectType;
    protected String objectName;
    protected ConnectionId connectionId;
    protected int overload;

    private WeakReference<T> reference;
    private int hashCode = -1;

    public DBObjectRef(ConnectionId connectionId, String identifier) {
        deserialize(connectionId, identifier);
    }

    public DBObjectRef(T object) {
        reference = new WeakReference<T>(object);
        objectType = object.getObjectType();
        objectName = object.getName();
        overload = object.getOverload();
        DBObject parentObj = object.getParentObject();
        if (parentObj != null) {
            parent = parentObj.getRef();
        } else {
            ConnectionHandler connectionHandler = object.getConnectionHandler();
            connectionId = connectionHandler.getId();
        }
    }

    public DBObjectRef(DBObjectRef parent, DBObjectType objectType, String objectName) {
        this.parent = parent;
        this.objectType = objectType;
        this.objectName = objectName;
    }

    public DBObjectRef(ConnectionId connectionId, DBObjectType objectType, String objectName) {
        this.connectionId = connectionId;
        this.objectType = objectType;
        this.objectName = objectName;
    }

    public DBObjectRef() {

    }

    public DBObject getParentObject(DBObjectType objectType) {
        DBObjectRef parentRef = getParentRef(objectType);
        return DBObjectRef.get(parentRef);
    }

    public DBObjectRef getParentRef(DBObjectType objectType) {
        DBObjectRef parent = this;
        while (parent != null) {
            if (parent.objectType.matches(objectType)) {
                return parent;
            }
            parent = parent.parent;
        }
        return null;
    }

    public static <T extends DBObject> DBObjectRef<T> from(Element element) {
        String objectRefDefinition = element.getAttributeValue("object-ref");
        if (StringUtil.isNotEmpty(objectRefDefinition)) {
            DBObjectRef<T> objectRef = new DBObjectRef<>();
            objectRef.readState(element);
            return objectRef;
        }
        return null;
    }

    public void readState(Element element) {
        if (element != null) {
            ConnectionId connectionId = ConnectionId.get(element.getAttributeValue("connection-id"));
            String objectIdentifier = element.getAttributeValue("object-ref");
            deserialize(connectionId, objectIdentifier);
        }
    }

    public void deserialize(ConnectionId connectionId, String objectIdentifier) {
        if (objectIdentifier.contains("]")) {
            deserializeOld(connectionId, objectIdentifier);
        } else {
            String[] tokens = objectIdentifier.split(PS);

            DBObjectRef objectRef = null;
            DBObjectType objectType = null;
            for (int i=0; i<tokens.length; i++) {
                String token = tokens[i];
                if (objectType == null) {
                    if (i == tokens.length -1) {
                        // last optional "overload" numeric token
                        this.overload = Integer.parseInt(token);
                    } else {
                        objectType = DBObjectType.forListName(token, objectRef == null ? null : objectRef.getObjectType());
                    }
                } else {
                    if (i < tokens.length - 2) {
                        objectRef = objectRef == null ?
                                new DBObjectRef(connectionId, objectType, token) :
                                new DBObjectRef(objectRef, objectType, token);
                    } else {
                        this.parent = objectRef;
                        this.objectType = objectType;
                        this.objectName = token;
                    }
                    objectType = null;
                }
            }
        }
    }

    @Deprecated
    private void deserializeOld(ConnectionId connectionId, String objectIdentifier) {
        int typeEndIndex = objectIdentifier.indexOf("]");
        StringTokenizer objectTypes = new StringTokenizer(objectIdentifier.substring(1, typeEndIndex), PS);

        int objectStartIndex = typeEndIndex + 2;
        int objectEndIndex = objectIdentifier.lastIndexOf("]");

        StringTokenizer objectNames = new StringTokenizer(objectIdentifier.substring(objectStartIndex, objectEndIndex), PS);

        DBObjectRef objectRef = null;
        while (objectTypes.hasMoreTokens()) {
            String objectTypeName = objectTypes.nextToken();
            String objectName = objectNames.nextToken();
            DBObjectType objectType = DBObjectType.get(objectTypeName);
            if (objectTypes.hasMoreTokens()) {
                objectRef = objectRef == null ?
                        new DBObjectRef(connectionId, objectType, objectName) :
                        new DBObjectRef(objectRef, objectType, objectName);
            } else {
                if (objectNames.hasMoreTokens()) {
                    String overloadToken = objectNames.nextToken();
                    this.overload = Integer.parseInt(overloadToken);
                }
                this.parent = objectRef;
                this.objectType = objectType;
                this.objectName = objectName;
            }
        }

    }

    public void writeState(Element element) {
        String value = serialize();

        element.setAttribute("connection-id", getConnectionId().id());
        element.setAttribute("object-ref", value);
    }

    @NotNull
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append(objectType.getListName());
        builder.append(PS);
        builder.append(objectName);

        DBObjectRef parent = this.parent;
        while (parent != null) {
            builder.insert(0, PS);
            builder.insert(0, parent.objectName);
            builder.insert(0, PS);
            builder.insert(0, parent.objectType.getListName());
            parent = parent.parent;
        }

        if (overload > 0) {
            builder.append(PS);
            builder.append(overload);
        }

        return builder.toString();
    }


    public String getPath() {
        DBObjectRef parent = this.parent;
        if (parent == null) {
            return objectName;
        } else {
            StringBuilder buffer = new StringBuilder(objectName);
            while(parent != null) {
                buffer.insert(0, ".");
                buffer.insert(0, parent.objectName);
                parent = parent.parent;
            }
            return buffer.toString();
        }
    }

    public String getQualifiedName() {
        return getPath();
    }

    /**
     * qualified object name without schema (e.g. PROGRAM.METHOD)
     */
    public String getQualifiedObjectName() {
        DBObjectRef parent = this.parent;
        if (parent == null || parent.objectType == DBObjectType.SCHEMA) {
            return objectName;
        } else {
            StringBuilder buffer = new StringBuilder(objectName);
            while(parent != null && parent.objectType != DBObjectType.SCHEMA) {
                buffer.insert(0, '.');
                buffer.insert(0, parent.objectName);
                parent = parent.parent;
            }
            return buffer.toString();
        }    }

    public String getQualifiedNameWithType() {
        return objectType.getName() + " \"" + getPath() + "\"";
    }

    public String getTypePath() {
        DBObjectRef parent = this.parent;
        if (parent == null) {
            return objectType.getName();
        } else {
            StringBuilder buffer = new StringBuilder(objectType.getName());
            while(parent != null) {
                buffer.insert(0, '.');
                buffer.insert(0, parent.objectType.getName());
                parent = parent.parent;
            }
            return buffer.toString();
        }
    }


    public ConnectionId getConnectionId() {
        return parent == null ? connectionId : parent.getConnectionId();
    }

    public DBObjectRef getParent() {
        return parent;
    }

    public boolean is(@NotNull T object) {
        return object.getRef().equals(this);
    }

    @Nullable
    public static <T extends DBObject> DBObjectRef<T> from(T object) {
        return object == null ? null : object.getRef();
    }

    @Nullable
    public static <T extends DBObject> T get(DBObjectRef<T> objectRef) {
        return objectRef == null ? null : objectRef.get();
    }

    @NotNull
    public static <T extends DBObject> T getnn(DBObjectRef<T> objectRef) {
        T object = get(objectRef);
        return FailsafeUtil.get(object);
    }

    public static List<DBObject> get(List<DBObjectRef> objectRefs) {
        List<DBObject> objects = new ArrayList<DBObject>(objectRefs.size());
        for (DBObjectRef objectRef : objectRefs) {
            objects.add(get(objectRef));
        }
        return objects;
    }

    public static List<DBObject> getnn(List<DBObjectRef> objectRefs) {
        List<DBObject> objects = new ArrayList<DBObject>(objectRefs.size());
        for (DBObjectRef objectRef : objectRefs) {
            objects.add(getnn(objectRef));
        }
        return objects;
    }

    public static List<DBObjectRef> from(List<DBObject> objects) {
        List<DBObjectRef> objectRefs = new ArrayList<DBObjectRef>(objects.size());
        for (DBObject object : objects) {
            objectRefs.add(from(object));
        }
        return objectRefs;
    }

    @Nullable
    public T get() {
        return load(null);
    }

    @Nullable
    public T ensure(long timeoutSeconds) {
        return SimpleTimeoutCall.invoke(timeoutSeconds, null, false, () -> get());
/*
        try {
            BackgroundMonitor.startTimeoutProcess();
            return get();
        } finally {
            BackgroundMonitor.endTimeoutProcess();
        }
*/

    }

    public T getnn(){
        return FailsafeUtil.get(get());
    }

    @Nullable
    public T get(Project project) {
        return load(project);
    }

    protected final T load(Project project) {
        T object = getObject();
        if (object == null) {
            synchronized (this) {
                object = getObject();
                if (object == null) {
                    clearReference();
                    ConnectionHandler connectionHandler =
                            project == null || project.isDisposed() ?
                                    ConnectionCache.findConnectionHandler(getConnectionId()) :
                                    ConnectionManager.getInstance(project).getConnectionHandler(getConnectionId());
                    if (connectionHandler != null && !connectionHandler.isDisposed() && connectionHandler.isEnabled()) {
                        object = lookup(connectionHandler);
                        if (object != null) {
                            reference = new WeakReference<T>(object);
                        }
                    }
                }
            }
        }
        return object;
    }

    private T getObject() {
        try {
            if (reference == null) {
                return null;
            }

            T object = reference.get();
            if (object == null || object.isDisposed()) {
                return null;
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    private void clearReference() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }


    @Nullable
    protected T lookup(@NotNull ConnectionHandler connectionHandler) {
        DBObject object = null;
        if (parent == null) {
            object = connectionHandler.getObjectBundle().getObject(objectType, objectName, overload);
        } else {
            DBObject parentObject = parent.get();
            if (parentObject != null) {
                object = parentObject.getChildObject(objectType, objectName, overload, true);
                DBObjectType genericType = objectType.getGenericType();
                if (object == null && genericType != objectType) {
                    object = parentObject.getChildObject(genericType, objectName, overload, true);
                }
            }
        }
        return (T) object;
    }

    @Nullable
    public ConnectionHandler lookupConnectionHandler() {
        return ConnectionCache.findConnectionHandler(getConnectionId());
    }

    @Nullable
    @Override
    public ConnectionHandler getConnectionHandler() {
        return lookupConnectionHandler();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof DBObjectRef) {
            DBObjectRef that = (DBObjectRef) o;
            int result = this.getConnectionId().id().compareTo(that.getConnectionId().id());
            if (result != 0) return result;

            if (this.parent != null && that.parent != null) {
                if (this.parent.equals(that.parent)) {
                    result = this.objectType.compareTo(that.objectType);
                    if (result != 0) return result;

                    int nameCompare = this.objectName.compareTo(that.objectName);
                    return nameCompare == 0 ? this.overload - that.overload : nameCompare;
                } else {
                    return this.parent.compareTo(that.parent);
                }
            } else if(this.parent == null && that.parent == null) {
                result = this.objectType.compareTo(that.objectType);
                if (result != 0) return result;

                return this.objectName.compareTo(that.objectName);
            } else if (this.parent == null) {
                return -1;
            } else if (that.parent == null) {
                return 1;
            }
        }
        return 0;
    }

    protected DBSchema getSchema() {
        return (DBSchema) getParentObject(DBObjectType.SCHEMA);
    }

    public String getSchemaName() {
        DBObjectRef schemaRef = getParentRef(DBObjectType.SCHEMA);
        return schemaRef == null ? null : schemaRef.objectName;
    }

    public int getOverload() {
        return overload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBObjectRef that = (DBObjectRef) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public String toString() {
        return getObjectName();
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            synchronized (this) {
                if (hashCode == -1) {
                    hashCode = (getConnectionId().id() + PS + serialize()).hashCode();
                }
            }
        }
        return hashCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getFileName() {
        if (overload == 0) {
            return objectName;
        } else {
            return objectName + PS + overload;
        }
    }

    public DBObjectType getObjectType() {
        return objectType;
    }

    public boolean isOfType(DBObjectType objectType) {
        return this.objectType.matches(objectType);
    }

    public boolean isLoaded() {
        return (parent == null || parent.isLoaded()) && reference != null;
    }
}
