package com.dbn.connection.config.tns;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TnsImportData {
    private TnsNames tnsNames;
    private TnsImportType importType;
    private boolean selectedOnly;
}
