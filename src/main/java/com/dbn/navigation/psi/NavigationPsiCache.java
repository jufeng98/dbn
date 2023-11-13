package com.dbn.navigation.psi;

import com.intellij.openapi.Disposable;

@Deprecated
public class NavigationPsiCache implements Disposable {
/*    private final Map<DBObjectRef, DBObjectPsiFile> objectPsiFiles = new THashMap<DBObjectRef, DBObjectPsiFile>();
    private final Map<DBObjectRef, DBObjectPsiDirectory> objectPsiDirectories = new THashMap<DBObjectRef, DBObjectPsiDirectory>();
    private final Map<DBObjectList, DBObjectListPsiDirectory> objectListPsiDirectories = new THashMap<DBObjectList, DBObjectListPsiDirectory>();
    private final DBConnectionPsiDirectory connectionPsiDirectory;

    public NavigationPsiCache(ConnectionHandler connection) {
        connectionPsiDirectory = new DBConnectionPsiDirectory(connection);
    }

    public DBConnectionPsiDirectory getConnectionPsiDirectory() {
        return connectionPsiDirectory;
    }

    private DBObjectPsiFile lookupPsiFile(DBObject object) {
        DBObjectRef objectRef = object.getRef();
        DBObjectPsiFile psiFile = objectPsiFiles.get(objectRef);
        if (psiFile == null) {
            synchronized (objectPsiFiles) {
                psiFile = objectPsiFiles.get(objectRef);
                if (psiFile == null) {
                    psiFile = new DBObjectPsiFile(object);
                    objectPsiFiles.put(objectRef, psiFile);
                }
            }
        }

        return psiFile;
    }

    private DBObjectPsiDirectory lookupPsiDirectory(DBObject object) {
        DBObjectRef objectRef = object.getRef();
        DBObjectPsiDirectory psiDirectory = objectPsiDirectories.get(objectRef);
        if (psiDirectory == null) {
            synchronized (objectPsiDirectories) {
                psiDirectory = objectPsiDirectories.get(objectRef);
                if (psiDirectory == null) {
                    psiDirectory = new DBObjectPsiDirectory(object);
                    objectPsiDirectories.put(objectRef, psiDirectory);
                }
            }
        }

        return psiDirectory;
    }
    
    private DBObjectListPsiDirectory lookupPsiDirectory(DBObjectList objectList) {
        DBObjectListPsiDirectory psiDirectory = objectListPsiDirectories.get(objectList);
        if (psiDirectory == null) {
            synchronized (objectListPsiDirectories) {
                psiDirectory = objectListPsiDirectories.get(objectList);
                if (psiDirectory == null) {
                    psiDirectory = new DBObjectListPsiDirectory(objectList);
                    objectListPsiDirectories.put(objectList, psiDirectory);
                }
            }
        }

        return psiDirectory;
    }
    
    
    public static DBObjectPsiFile getPsiFile(DBObject object) {
        object = FailsafeUtil.get(object);
        ConnectionHandler connection = FailsafeUtil.get(object.getCache());
        NavigationPsiCache psiCache = connection.getPsiCache();
        return psiCache.lookupPsiFile(object);
    }

    public static DBObjectPsiDirectory getPsiDirectory(DBObject object) {
        object = FailsafeUtil.get(object);
        ConnectionHandler connection = FailsafeUtil.get(object.getCache());
        NavigationPsiCache psiCache = connection.getPsiCache();
        return psiCache.lookupPsiDirectory(object);
    }
    
    public static DBObjectListPsiDirectory getPsiDirectory(DBObjectList objectList) {
        return objectList == null ? null :
                objectList.getCache().getPsiCache().lookupPsiDirectory(objectList);
    }

    public static DBConnectionPsiDirectory getPsiDirectory(ConnectionHandler connection) {
        return connection.getPsiCache().connectionPsiDirectory;
    }*/

    @Override
    public void dispose() {
/*
        DisposerUtil.dispose(connectionPsiDirectory);
        DisposerUtil.dispose(objectListPsiDirectories);
        DisposerUtil.dispose(objectPsiDirectories);
        DisposerUtil.dispose(objectPsiFiles);
*/
    }
}
