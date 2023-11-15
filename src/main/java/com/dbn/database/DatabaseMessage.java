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

    public DatabaseMessage(String title, String details) {
        this.title = title;
        this.details = details;
    }
}
