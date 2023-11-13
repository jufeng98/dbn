package com.dbn.execution.script;

import com.dbn.common.options.PersistentConfiguration;
import com.dbn.common.ui.Presentable;
import com.dbn.common.util.Cloneable;
import com.dbn.common.util.Commons;
import com.dbn.common.util.Strings;
import com.dbn.connection.DatabaseType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jdesktop.swingx.util.OS;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.UUID;

import static com.dbn.common.options.setting.Settings.enumAttribute;
import static com.dbn.common.options.setting.Settings.stringAttribute;

@Data
@EqualsAndHashCode(callSuper = false)
public class CmdLineInterface implements Cloneable<CmdLineInterface>, PersistentConfiguration, Presentable {
    public static final String DEFAULT_ID = "DEFAULT";

    private DatabaseType databaseType;
    private String executablePath;
    private String id;
    private String name;
    private String description;

    public interface Defaults {
        String extension = OS.isWindows() ? ".exe" : "";
        CmdLineInterface ORACLE = new CmdLineInterface(DEFAULT_ID, DatabaseType.ORACLE, "sqlplus", "Oracle SQL*Plus client", "sqlplus" + extension + "");
        CmdLineInterface MYSQL = new CmdLineInterface(DEFAULT_ID, DatabaseType.MYSQL, "mysql", "MySQL client", "mysql" + extension + "");
        CmdLineInterface POSTGRES = new CmdLineInterface(DEFAULT_ID, DatabaseType.POSTGRES, "psql ", "PostgreSQL terminal", "psql" + extension);
        CmdLineInterface SQLITE = new CmdLineInterface(DEFAULT_ID, DatabaseType.SQLITE, "sqlite3 ", "SQLite terminal", "sqlite3" + extension);
        CmdLineInterface GENERIC = new CmdLineInterface(DEFAULT_ID, DatabaseType.GENERIC, "sql ", "SQL terminal", "sql" + extension);
    }

    public static CmdLineInterface getDefault(@Nullable DatabaseType databaseType) {
        if (databaseType != null) {
            switch (databaseType) {
                case ORACLE: return Defaults.ORACLE;
                case MYSQL: return Defaults.MYSQL;
                case POSTGRES: return Defaults.POSTGRES;
                case SQLITE: return Defaults.SQLITE;
            }
        }
        return Defaults.GENERIC;
    }

    public CmdLineInterface() {

    }

    public CmdLineInterface(DatabaseType databaseType, String executablePath, String name, String description) {
        this(UUID.randomUUID().toString(), databaseType, executablePath, name, description);
    }

    public CmdLineInterface(String id, DatabaseType databaseType, String executablePath, String name, String description) {
        this.id = id;
        this.name = name;
        this.databaseType = databaseType;
        this.executablePath = executablePath;
        this.description = description;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return databaseType.getIcon();
    }

    @NotNull
    @Override
    public String getName() {
        return Commons.nvl(name, "");
    }

    @Nullable
    @Override
    public String getDescription() {
        return Commons.nvl(description, executablePath);
    }

    @Override
    public void readConfiguration(Element element) {
        id = stringAttribute(element, "id");
        if (Strings.isEmpty(id)) id = UUID.randomUUID().toString();
        name = stringAttribute(element, "name");
        executablePath = stringAttribute(element, "executable-path");
        databaseType = enumAttribute(element, "database-type", DatabaseType.class);
    }

    @Override
    public void writeConfiguration(Element element) {
        element.setAttribute("id", id);
        element.setAttribute("name", name);
        element.setAttribute("executable-path", executablePath);
        element.setAttribute("database-type", databaseType.id());
    }

    @Override
    public CmdLineInterface clone() {
        return new CmdLineInterface(id, databaseType, executablePath, name, description);
    }
}
