package com.dbn.language.sql.dialect.oracle;

import com.dbn.language.common.lexer.DBLanguageCompoundLexer;

import java.util.Deque;
import java.util.LinkedList;

public final class OraclePLSQLBlockMonitor {
    public enum Marker {
        CASE,
        BEGIN,
        CREATE,
        DECLARE,
        PROGRAM}

    private final DBLanguageCompoundLexer lexer;
    private final Deque<Marker> stack = new LinkedList<>();
    private final int initialState;
    private final int psqlBlockState;
    private boolean log = false;

    private int blockStart;

    public OraclePLSQLBlockMonitor(DBLanguageCompoundLexer lexer, int initialState, int psqlBlockState) {
        this.lexer = lexer;
        this.initialState = initialState;
        this.psqlBlockState = psqlBlockState;
    }

    public void ignore() {
        if (log) System.out.println("ignore:    " + lexer.getCurrentToken());
    }


    public void start(Marker marker) {
        if (log) System.out.println("start:     " + lexer.getCurrentToken());
        if (!stack.isEmpty()) {
            stack.clear();
        }
        stack.push(marker);

        // PLSQL block start
        lexer.yybegin(psqlBlockState);
        blockStart = lexer.getTokenStart();
    }

    public void mark(Marker marker) {
        if (log) System.out.println("mark:      " + lexer.getCurrentToken());
        stack.push(marker);
    }

    public boolean end(boolean force) {
        if (force) {
            if (log) System.out.println("end force: " + lexer.getCurrentToken());
            stack.clear();
        } else {
            if (log) System.out.println("end:       " + lexer.getCurrentToken());
            Marker marker = stack.poll();
            if (marker == Marker.BEGIN) {
                if (!stack.isEmpty()) {
                    Marker previousMarker = stack.peek();
                    if (previousMarker == Marker.DECLARE || previousMarker == Marker.CREATE) {
                        stack.poll();
                    }
                }
            } else {
                while (marker == Marker.PROGRAM) {
                    marker = stack.poll();
                }
            }
        }

        if (stack.isEmpty()) {
            // PLSQL block end
            lexer.yybegin(initialState);
            lexer.setTokenStart(blockStart);
            return true;
        }

        return false;
    }

    public void pushBack() {
        lexer.yypushback(lexer.yylength());
    }

    public boolean isBlockStarted() {
        return blockStart < lexer.getCurrentPosition();
    }


    public void reset() {
        stack.clear();
    }

}
