package com.dbn.generator;

import com.dbn.connection.ConnectionContext;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.DatabaseInterfacesBundle;
import com.dbn.connection.PooledConnection;
import com.dbn.database.interfaces.DatabaseInterfaces;
import com.dbn.database.interfaces.DatabaseMetadataInterface;
import com.dbn.object.DBTable;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.project.Project;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class CreateTableStatementGenerator extends StatementGenerator {
    private final DBObjectRef<DBTable> table;

    public CreateTableStatementGenerator(DBTable table) {
        this.table = DBObjectRef.of(table);
    }

    public DBTable getTable() {
        return table.ensure();
    }

    @Override
    @SneakyThrows
    public StatementGeneratorResult generateStatement(Project project) {
        DBTable table = getTable();

        StatementGeneratorResult result = new StatementGeneratorResult();

        DatabaseInterfaces databaseInterfaces = DatabaseInterfacesBundle.get(table.getConnection());
        DatabaseMetadataInterface metadataInterface = databaseInterfaces.getMetadataInterface();
        ConnectionHandler connectionHandler = table.getConnection();

        ConnectionContext connectionContext = connectionHandler.createConnectionContext();
        ConnectionContext.surround(connectionContext, () ->
                PooledConnection.run(connectionHandler.createConnectionContext(), conn -> {
                    ResultSet resultSet = metadataInterface.showCreateTable(table.getName(), conn);
                    resultSet.next();
                    String s = resultSet.getString("Create Table");
                    result.setStatement(s);
                    resultSet.close();
                }));

        return result;
    }
}
