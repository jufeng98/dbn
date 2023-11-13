package com.dbn.language.sql.dialect.mysql;

import com.dbn.language.sql.SQLParserDefinition;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class MysqlSQLParserDefinition extends SQLParserDefinition {

    MysqlSQLParserDefinition(MysqlSQLParser parser) {
        super(parser);
    }

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MysqlSQLParserFlexLexer(getTokenTypes()));
    }

}