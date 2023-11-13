package com.dbn.data.export.processor;

import com.dbn.connection.ConnectionHandler;
import com.dbn.data.export.DataExportException;
import com.dbn.data.export.DataExportFormat;
import com.dbn.data.export.DataExportInstructions;
import com.dbn.data.export.DataExportModel;

public class CSVDataExportProcessor extends CustomDataExportProcessor{
    @Override
    public DataExportFormat getFormat() {
        return DataExportFormat.CSV;
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    public void performExport(DataExportModel model, DataExportInstructions instructions, ConnectionHandler connection) throws DataExportException {
        instructions.setValueSeparator(",");
        super.performExport(model, instructions, connection);
    }
}
