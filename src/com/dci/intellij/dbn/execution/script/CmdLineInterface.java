package com.dci.intellij.dbn.execution.script;

import javax.swing.Icon;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.options.PersistentConfiguration;
import com.dci.intellij.dbn.common.ui.Presentable;
import com.dci.intellij.dbn.common.util.Cloneable;
import com.dci.intellij.dbn.common.util.CommonUtil;
import com.dci.intellij.dbn.connection.DatabaseType;

public class CmdLineInterface extends CommonUtil implements Cloneable<CmdLineInterface>, PersistentConfiguration, Presentable {
    public static final String DEFAULT_ID = "DEFAULT";

    private DatabaseType databaseType;
    private String executablePath;
    private String id;
    private String name;
    private String description;

    public interface Defaults {
        CmdLineInterface ORACLE = new CmdLineInterface(DEFAULT_ID, DatabaseType.ORACLE, "sqlplus", "Oracle SQL*Plus client", "environment path based");
        CmdLineInterface MYSQL = new CmdLineInterface(DEFAULT_ID, DatabaseType.MYSQL, "mysql", "MySQL client", "environment path based");
        CmdLineInterface POSTGRES = new CmdLineInterface(DEFAULT_ID, DatabaseType.POSTGRES, "psql ", "PostgreSQL terminal - psql", "environment path based");
    }

    public static CmdLineInterface getDefault(@Nullable DatabaseType databaseType) {
        if (databaseType != null) {
            switch (databaseType) {
                case ORACLE: return Defaults.ORACLE;
                case MYSQL: return Defaults.MYSQL;
                case POSTGRES: return Defaults.POSTGRES;
            }
        }
        return null;
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

    public String getId() {
        return id;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return databaseType.getIcon();
    }

    @NotNull
    @Override
    public String getName() {
        return CommonUtil.nvl(name, "");
    }

    @Nullable
    @Override
    public String getDescription() {
        return CommonUtil.nvl(description, executablePath);
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void readConfiguration(Element element) {
        id = element.getAttributeValue("id");
        if (StringUtils.isEmpty(id)) id = UUID.randomUUID().toString();
        name = element.getAttributeValue("name");
        executablePath = element.getAttributeValue("executable-path");
        databaseType = DatabaseType.get(element.getAttributeValue("database-type"));
    }

    @Override
    public void writeConfiguration(Element element) {
        element.setAttribute("id", id);
        element.setAttribute("name", name);
        element.setAttribute("executable-path", executablePath);
        element.setAttribute("database-type", databaseType.name());
    }

    @Override
    public CmdLineInterface clone() {
        return new CmdLineInterface(id, databaseType, executablePath, name, description);
    }
}
