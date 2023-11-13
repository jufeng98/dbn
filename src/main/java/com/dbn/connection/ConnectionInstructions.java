package com.dbn.connection;

import lombok.Data;

@Data
public class ConnectionInstructions {
    private boolean allowAutoConnect;
    private boolean allowAutoInit;
}
