package com.dbn.object;

import java.util.List;

public interface DBProcedure extends DBMethod {
    @Override
    List<DBArgument> getArguments();

    @Override
    DBArgument getArgument(String name);
}