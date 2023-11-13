package com.dbn.data.export;

import com.dbn.common.state.PersistentStateElement;
import com.dbn.common.util.Cloneable;
import lombok.Data;
import org.jdom.Element;

import java.io.File;
import java.nio.charset.Charset;

import static com.dbn.common.options.setting.Settings.*;

@Data
public class DataExportInstructions implements PersistentStateElement, Cloneable<DataExportInstructions> {
    private boolean createHeader = true;
    private boolean friendlyHeaders = false;
    private boolean quoteValuesContainingSeparator = true;
    private boolean quoteAllValues = false;
    private String valueSeparator;
    private String beginQuote = "\"";
    private String endQuote = "\"";
    private String fileName;
    private String fileLocation;
    private Scope scope = Scope.GLOBAL;
    private Destination destination = Destination.FILE;
    private DataExportFormat format = DataExportFormat.EXCEL;
    private String baseName;
    private Charset charset = Charset.defaultCharset();

    public File getFile() {
        return new File(fileLocation, fileName);
    }

    public enum Scope{
        GLOBAL,
        SELECTION
    }

    public enum Destination{
        FILE,
        CLIPBOARD
    }

    @Override
    public DataExportInstructions clone() {
        return PersistentStateElement.cloneElement(this, new DataExportInstructions());
    }

    /***********************************************
     *            PersistentStateElement           *
     ***********************************************/
    @Override
    public void writeState(Element element) {
        Element child = newElement(element, "export-instructions");

        setBoolean(child, "create-header", createHeader);
        setBoolean(child, "friendly-headers", friendlyHeaders);
        setBoolean(child, "quote-values-containing-separator", quoteValuesContainingSeparator);
        setBoolean(child, "quote-all-values", quoteAllValues);
        setString(child, "value-separator", valueSeparator);
        setString(child, "begin-quote", beginQuote);
        setString(child, "end-quote", endQuote);
        setString(child, "file-name", fileName);
        setString(child, "file-location", fileLocation);
        setEnum(child, "scope", scope);
        setEnum(child, "destination", destination);
        setEnum(child, "format", format);
        setString(child, "charset", charset.name());
        setString(child, "charset", charset.name());
    }

    @Override
    public void readState(Element element) {
        Element child = element.getChild("export-instructions");
        if (child == null) return;

        createHeader = getBoolean(child, "create-header", createHeader);
        friendlyHeaders = getBoolean(child, "friendly-headers", friendlyHeaders);
        quoteValuesContainingSeparator = getBoolean(child, "quote-values-containing-separator", quoteValuesContainingSeparator);
        quoteAllValues = getBoolean(child, "quote-all-values", quoteAllValues);
        beginQuote = getString(child, "begin-quote", beginQuote);
        endQuote = getString(child, "end-quote", endQuote);
        valueSeparator = getString(child, "value-separator", valueSeparator);
        fileName = getString(child, "file-name", fileName);
        fileLocation = getString(child, "file-location", fileLocation);
        scope = getEnum(child, "scope", scope);
        destination = getEnum(child, "destination", destination);
        format = getEnum(child, "format", format);
        charset = Charset.forName(getString(element, "charset", charset.name()));
    }
}
