package com.dbn.driver;

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.List;

public interface DriverClassLoader {
    File getLibrary();

    default List<File> getJars() {
        return null;
    }

    List<Class<Driver>> getDrivers();

    default void close() throws IOException {}
}
