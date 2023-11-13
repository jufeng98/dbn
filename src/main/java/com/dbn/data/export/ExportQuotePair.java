package com.dbn.data.export;

import com.dbn.common.util.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExportQuotePair {
    private static final List<ExportQuotePair> REGISTRY = new ArrayList<>();
    static {
        new ExportQuotePair("’");
        new ExportQuotePair("'");
        new ExportQuotePair("\"");
        new ExportQuotePair("|");
        new ExportQuotePair("(", ")");
        new ExportQuotePair("[", "]");
        new ExportQuotePair("{", "}");
        new ExportQuotePair("<", ">");
        new ExportQuotePair("\u00AB", "\u00BB");
        new ExportQuotePair("\u2018", "\u2019");
        new ExportQuotePair("\u201A", "\u201B");
        new ExportQuotePair("\u201C", "\u201D");
        new ExportQuotePair("\u201E", "\u201F");
        new ExportQuotePair("\u2039", "\u203A");
        new ExportQuotePair("\u2E42");
        new ExportQuotePair("\u231C", "\u231D");
        new ExportQuotePair("\u275B", "\u301E");
        new ExportQuotePair("\u275D", "\u275E");
        new ExportQuotePair("\u301D", "\u301E");
        new ExportQuotePair("\u301F");
        new ExportQuotePair("\uFF02");
        new ExportQuotePair("\uFF07");
    }

    private String beginQuote;
    private String endQuote;

    public ExportQuotePair(String quote) {
        this(quote, quote);
    }

    public ExportQuotePair(String beginQuote, String endQuote) {
        this.beginQuote = beginQuote;
        this.endQuote = endQuote;
        REGISTRY.add(this);
    }

    public static String endQuoteOf(String beginQuote) {
        ExportQuotePair pair = Lists.first(REGISTRY, q -> q.beginQuote.equals(beginQuote));
        if (pair != null) return pair.getEndQuote();

        pair = Lists.first(REGISTRY, q -> q.endQuote.equals(beginQuote));
        if (pair != null) return pair.getBeginQuote();

        return beginQuote;
    }
}
