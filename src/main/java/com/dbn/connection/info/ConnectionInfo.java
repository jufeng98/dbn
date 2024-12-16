package com.dbn.connection.info;

import com.dbn.common.util.Unsafe;
import com.dbn.connection.DatabaseType;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static com.dbn.common.util.Strings.toLowerCase;

@Data
public class ConnectionInfo {
    private final DatabaseType databaseType;
    private final String productName;
    private final String productVersion;
    private final String driverName;
    private final String driverVersion;
    private final String driverJdbcType;
    private final String url;
    private final String userName;

    public ConnectionInfo(DatabaseMetaData metaData) throws SQLException {
        productName = metaData.getDatabaseProductName();
        productVersion = resolveProductVersion(metaData);
        driverName = Unsafe.silent("UNKNOWN", metaData, DatabaseMetaData::getDriverName);
        driverVersion = Unsafe.silent("UNKNOWN", metaData, DatabaseMetaData::getDriverVersion);
        url = metaData.getURL();
        userName = metaData.getUserName();
        driverJdbcType = resolveDriverType(metaData);
        databaseType = DatabaseType.resolve(toLowerCase(productName));
    }

    @NotNull
    private static String resolveDriverType(DatabaseMetaData metaData) {
        int majorVersion = Unsafe.silent(0, metaData, DatabaseMetaData::getJDBCMajorVersion);
        int minorVersion = Unsafe.silent(0, metaData, DatabaseMetaData::getJDBCMinorVersion);
        return majorVersion + (minorVersion > 0 ? "." + minorVersion : "");
    }

    @NotNull
    private static String resolveProductVersion(DatabaseMetaData metaData) {
        String productVersion = Unsafe.silent("UNKNOWN", metaData, DatabaseMetaData::getDatabaseProductVersion);
        int index = productVersion.indexOf('\n');
        productVersion = index > -1 ? productVersion.substring(0, index) : productVersion;
        return productVersion.trim();
    }

}
