package com.dci.intellij.dbn.data.value;

import javax.sql.rowset.serial.SerialClob;
import java.sql.NClob;
import java.sql.SQLException;

public class SerialNClob extends SerialClob implements NClob {
    public SerialNClob(char[] ch) throws SQLException {
        super(ch);
    }

}
