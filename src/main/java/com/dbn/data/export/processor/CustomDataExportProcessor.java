package com.dbn.data.export.processor;

import com.dbn.common.load.ProgressMonitor;
import com.dbn.common.locale.Formatter;
import com.dbn.connection.ConnectionHandler;
import com.dbn.data.export.DataExportException;
import com.dbn.data.export.DataExportFormat;
import com.dbn.data.export.DataExportInstructions;
import com.dbn.data.export.DataExportModel;

public class CustomDataExportProcessor extends DataExportProcessor{
    @Override
    public DataExportFormat getFormat() {
        return DataExportFormat.CUSTOM;
    }

    @Override
    public boolean supports(DataExportFeature feature) {
        return feature.isOneOf(
                DataExportFeature.HEADER_CREATION,
                DataExportFeature.FRIENDLY_HEADER,
                DataExportFeature.EXPORT_TO_FILE,
                DataExportFeature.EXPORT_TO_CLIPBOARD,
                DataExportFeature.VALUE_QUOTING,
                DataExportFeature.FILE_ENCODING);
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    public void performExport(DataExportModel model, DataExportInstructions instructions, ConnectionHandler connection) throws DataExportException {
        StringBuilder buffer = new StringBuilder();
        Formatter formatter = getFormatter(connection.getProject());

        createHeader(model, instructions, buffer);
        createContent(model, instructions, formatter, buffer);
        writeContent(instructions, buffer.toString());
    }

    private void createHeader(DataExportModel model, DataExportInstructions instructions, StringBuilder buffer) throws DataExportException {
        if (!instructions.isCreateHeader()) return;

        String beginQuote = instructions.getBeginQuote();
        String endQuote = instructions.getEndQuote();
        for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++){
            String columnName = getColumnName(model, instructions, columnIndex);
            String separator = instructions.getValueSeparator();
            boolean containsSeparator = columnName.contains(separator);
            boolean quote =
                    instructions.isQuoteAllValues() || (
                    instructions.isQuoteValuesContainingSeparator() && containsSeparator);

            if (containsSeparator && !quote) {
                throw new DataExportException(
                        "Can not create columns header with the given separator.\n" +
                                "Column " + columnName + " already contains the separator '" + separator + "'. \n" +
                                "Please consider quoting.");
            }

            if (columnIndex > 0) {
                buffer.append(separator);
            }

            if (quote) {
                if(columnName.contains(beginQuote) || columnName.contains(endQuote)) {
                    throw new DataExportException(
                            "Can not quote columns header.\n" +
                            "Column " + columnName + " contains quotes.");
                }
                buffer.append(beginQuote);
                buffer.append(columnName);
                buffer.append(endQuote);
            } else {
                buffer.append(columnName);
            }
        }
        buffer.append('\n');
    }

    private void createContent(DataExportModel model, DataExportInstructions instructions, Formatter formatter, StringBuilder buffer) throws DataExportException {
        String beginQuote = instructions.getBeginQuote();
        String endQuote = instructions.getEndQuote();

        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                ProgressMonitor.checkCancelled();
                String columnName = getColumnName(model, instructions, c);
                Object object = model.getValue(r, c);
                String value = formatValue(formatter, object);
                String separator = instructions.getValueSeparator();

                boolean containsSeparator = value.contains(separator);
                boolean quote =
                        instructions.isQuoteAllValues() || (
                        instructions.isQuoteValuesContainingSeparator() && containsSeparator);

                if (containsSeparator && !quote) {
                    throw new DataExportException(
                            "Can not create row " + (r + 1) + " with the given separator.\n" +
                                    "Value for column " + columnName + " already contains the separator '" + separator + "'. \n" +
                                    "Please consider quoting.");
                }

                if (c > 0) {
                    buffer.append(separator);
                }

                if (quote) {
                    if (value.contains(beginQuote) || value.contains(endQuote)) {
                        throw new DataExportException(
                                "Can not quote value of " + columnName + " at row " + (r + 1) + ".\n" +
                                "Value contains quotes itself.");
                    }
                    buffer.append(beginQuote);
                    buffer.append(value);
                    buffer.append(endQuote);
                } else {
                    buffer.append(value);
                }
            }
            buffer.append('\n');
        }
    }

}
