package com.dbn.database.oracle;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.database.common.DatabaseMetadataInterfaceImpl;
import com.dbn.database.interfaces.DatabaseInterfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OracleMetadataInterface extends DatabaseMetadataInterfaceImpl {

    public OracleMetadataInterface(DatabaseInterfaces provider) {
        super("oracle_metadata_interface.xml", provider);
    }

    @Override
    public ResultSet loadDatabaseTriggerSourceCode(String ownerName, String triggerName, DBNConnection connection) throws SQLException {
        return loadObjectSourceCode(ownerName, triggerName, "TRIGGER", connection);
    }

    @Override
    public ResultSet loadDatasetTriggerSourceCode(String tableOwner, String tableName, String ownerName, String triggerName, DBNConnection connection) throws SQLException {
        return loadObjectSourceCode(ownerName, triggerName, "TRIGGER", connection);
    }

    public void loadRegionalSettings() {
        // TODO oracle "Locale not recognized"
        //String language = CharacterSetMetaData.getNLSLanguage(Locale.getDefault(Locale.Category.FORMAT));
        //String territory = CharacterSetMetaData.getNLSTerritory(Locale.getDefault(Locale.Category.FORMAT));
    }

    @Override
    public String createDateString(Date date) {
        String dateString = META_DATE_FORMAT.get().format(date);
        return "to_date('" + dateString + "', 'yyyy-mm-dd HH24:MI:SS')";
    }


}
