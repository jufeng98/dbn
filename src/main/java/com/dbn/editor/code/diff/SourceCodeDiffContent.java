package com.dbn.editor.code.diff;

import lombok.Getter;

@Getter
public class SourceCodeDiffContent {
    private final String title;
    private final byte[] byteContent;

    public SourceCodeDiffContent(String title, CharSequence content) {
        this.title = title;
        this.byteContent = content.toString().getBytes();
    }

    public String getTitle() {
        return title;
    }

    public byte[] getByteContent() {
        return byteContent;
    }
}
