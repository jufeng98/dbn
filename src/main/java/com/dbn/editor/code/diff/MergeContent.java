package com.dbn.editor.code.diff;

import java.util.ArrayList;
import java.util.List;

public class MergeContent {
    private final List<SourceCodeDiffContent> contents = new ArrayList<>();

    public MergeContent(SourceCodeDiffContent leftContent, SourceCodeDiffContent targetContent, SourceCodeDiffContent rightContent) {
        contents.add(leftContent);
        contents.add(targetContent);
        contents.add(rightContent);
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for (SourceCodeDiffContent content : contents) {
            titles.add(content.getTitle());
        }

        return titles;
    }

    public List<byte[]> getByteContents() {
        List<byte[]> byteContents = new ArrayList<>();
        for (SourceCodeDiffContent content : contents) {
            byteContents.add(content.getByteContent());
        }

        return byteContents;
    }

}
