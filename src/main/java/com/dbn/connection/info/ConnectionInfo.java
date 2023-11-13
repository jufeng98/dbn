package com.dbn.connection.info;

import com.dbn.common.util.Unsafe;
import com.dbn.connection.DatabaseType;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Value
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
        driverName = Unsafe.silent("UNKNOWN", metaData, md -> md.getDriverName());
        driverVersion = Unsafe.silent("UNKNOWN", metaData, md -> md.getDriverVersion());
        url = metaData.getURL();
        userName = metaData.getUserName();
        driverJdbcType = resolveDriverType(metaData);
        databaseType = DatabaseType.resolve(productName.toLowerCase());
    }

    @NotNull
    private static String resolveDriverType(DatabaseMetaData metaData) throws SQLException {
        int majorVersion = Unsafe.silent(0, metaData, md -> md.getJDBCMajorVersion());
        int minorVersion = Unsafe.silent(0, metaData, md -> md.getJDBCMinorVersion());
        return majorVersion + (minorVersion > 0 ? "." + minorVersion : "");
    }

    @NotNull
    private static String resolveProductVersion(DatabaseMetaData metaData) throws SQLException {
        String productVersion = Unsafe.silent("UNKNOWN", metaData, md -> md.getDatabaseProductVersion());
        int index = productVersion.indexOf('\n');
        productVersion = index > -1 ? productVersion.substring(0, index) : productVersion;
        return productVersion.trim();
    }

    public String toString() {
        return  "Product name:\t" + productName + '\n' +
                "Product version:\t" + productVersion + '\n' +
                "Driver name:\t\t" + driverName + '\n' +
                "Driver version:\t" + driverVersion + '\n'+
                "JDBC Type:\t\t" + driverJdbcType + '\n' +
                "URL:\t\t" + url + '\n' +
                "User name:\t\t" + userName;
    }
}
