package com.dbn.connection.config.tns;

import com.dbn.common.text.TextContent;
import com.dbn.common.ui.Presentable;
import com.dbn.common.util.Commons;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@Getter
public enum TnsImportType implements Presentable {
    FIELDS("Fields", loadInfo("tns_import_type_fields.html")),
    PROFILE("Profile", loadInfo("tns_import_type_profile.html")),
    DESCRIPTOR("Descriptor", loadInfo("tns_import_type_descriptor.html"));

    private final String name;
    private final TextContent info;

    TnsImportType(String name, TextContent info) {
        this.name = name;
        this.info = info;
    }

    @NotNull
    @SneakyThrows
    private static TextContent loadInfo(String fileName) {
        String content = Commons.readInputStream(TnsImportType.class.getResourceAsStream(fileName));
        return TextContent.html(content);
    }
}
