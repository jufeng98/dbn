package com.dbn.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DatabaseMessage {
    private String title;
    private String details;

    @Getter(lazy = true)
    private final String tooltip = buildTooltip();

    public DatabaseMessage(String title, String details) {
        this.title = title;
        this.details = details;
    }

    private String buildTooltip() {
        if (details == null) return null;
        return "<html><div style='white-space:nowrap'>" + details.replaceAll("\n", "<br>") + "</div></html>";
    }
}
