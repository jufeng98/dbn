package com.dci.intellij.dbn.data.type;

import com.dci.intellij.dbn.common.dispose.Disposer;
import com.dci.intellij.dbn.common.dispose.StatefulDisposableBase;
import com.dci.intellij.dbn.common.dispose.UnlistedDisposable;
import com.dci.intellij.dbn.common.latent.Latent;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.ConnectionRef;
import com.dci.intellij.dbn.database.interfaces.DatabaseInterfaces;
import com.dci.intellij.dbn.object.DBPackage;
import com.dci.intellij.dbn.object.DBSchema;
import com.dci.intellij.dbn.object.DBType;
import com.dci.intellij.dbn.object.common.DBObjectBundle;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dci.intellij.dbn.common.util.Strings.cachedUpperCase;

public final class DBDataTypeBundle extends StatefulDisposableBase implements UnlistedDisposable {
    private final ConnectionRef connection;

    private final Latent<Map<String, DBNativeDataType>> nativeDataTypes = Latent.basic(() -> createNativeDataTypes());
    private final Map<DBDataTypeDefinition, DBDataType> dataTypes = new ConcurrentHashMap<>();

    public DBDataTypeBundle(@NotNull ConnectionHandler connection) {
        this.connection = connection.ref();
    }

    @NotNull
    public ConnectionHandler getConnection() {
        return ConnectionRef.ensure(connection);
    }

    private Map<String, DBNativeDataType> getNativeDataTypes() {
        checkDisposed();
        return nativeDataTypes.get();
    }

    public DBNativeDataType getNativeDataType(String name) {
        if (name == null) return null;

        String upperCaseName = cachedUpperCase(name);
        Map<String, DBNativeDataType> dataTypes = getNativeDataTypes();

        DBNativeDataType dataType = dataTypes.get(upperCaseName);
        if (dataType != null) return dataType;

        for (val entry : dataTypes.entrySet()) {
            String key = entry.getKey();
            DBNativeDataType value = entry.getValue();
            if (key.startsWith(upperCaseName)) {
                return value;
            }
        }
        return null;
    }


    private Map<String, DBNativeDataType> createNativeDataTypes() {
        Map<String, DBNativeDataType> nativeDataTypes = new HashMap<>();

        DatabaseInterfaces interfaces = getConnection().getInterfaces();
        List<DataTypeDefinition> dataTypeDefinitions = interfaces.getNativeDataTypes().list();
        for (DataTypeDefinition dataTypeDefinition : dataTypeDefinitions) {
            DBNativeDataType dataType = new DBNativeDataType(dataTypeDefinition);
            nativeDataTypes.put(cachedUpperCase(dataType.getName()), dataType);
        }
        return nativeDataTypes;
    }

    public DBDataType getDataType(DBDataTypeDefinition definition) {
        return dataTypes.computeIfAbsent(definition, d -> createDataType(d));
    }

    private DBDataType createDataType(DBDataTypeDefinition def) {
        checkDisposed();
        String name = null;
        DBType declaredType = null;
        DBNativeDataType nativeDataType = null;

        DBObjectBundle objectBundle = getConnection().getObjectBundle();
        String declaredTypeOwner = def.getDeclaredTypeOwner();
        String declaredTypeProgram = def.getDeclaredTypeProgram();
        String declaredTypeName = def.getDeclaredTypeName();
        String dataTypeName = def.getDataTypeName();

        if (declaredTypeOwner != null) {
            DBSchema typeSchema = objectBundle.getSchema(declaredTypeOwner);
            if (typeSchema != null) {

                if (declaredTypeProgram != null) {
                    DBPackage packagee = typeSchema.getPackage(declaredTypeProgram);
                    if (packagee != null) {
                        declaredType = packagee.getType(declaredTypeName);
                    } /*else {
                        DBType type = typeSchema.getType(declaredTypeProgram);
                        if (type != null) {
                            declaredType = packagee.getType(declaredTypeName);
                        }
                    }*/
                } else {
                    declaredType = typeSchema.getType(declaredTypeName);
                }
            }
            if (declaredType == null) {
                name = declaredTypeName;
            }

            DBNativeDataType nDataType = objectBundle.getNativeDataType(dataTypeName);
            if (nDataType != null && nDataType.getDefinition().isPseudoNative()) {
                nativeDataType = nDataType;
            }

        } else {
            nativeDataType = objectBundle.getNativeDataType(dataTypeName);
            if (nativeDataType == null) name = dataTypeName;
        }

        DBDataType dataType = new DBDataType();
        dataType.setNativeType(nativeDataType);
        dataType.setDeclaredType(declaredType);
        dataType.setName(name);
        dataType.setLength(def.getLength());
        dataType.setPrecision(def.getPrecision());
        dataType.setScale(def.getScale());
        dataType.setSet(def.isSet());
        return dataType;
    }


    @Override
    protected void disposeInner() {
        nativeDataTypes.reset();
        Disposer.dispose(dataTypes);
    }
}
