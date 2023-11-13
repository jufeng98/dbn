package com.dbn.connection.config.ui;

import com.dbn.common.ui.Presentable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Driver;
import java.util.List;
import java.util.Objects;

@Getter
public class DriverOption implements Presentable {
    private final Class<Driver> driver;

    public DriverOption(Class<Driver> driver) {
        this.driver = driver;
    }

    @NotNull
    @Override
    public String getName() {
        return driver.getName();
    }

    public static DriverOption get(List<DriverOption> driverOptions, String name) {
        for (DriverOption driverOption : driverOptions) {
            if (Objects.equals(driverOption.getName(), name)) {
                return driverOption;
            }
        }
        return null;
    }
}
