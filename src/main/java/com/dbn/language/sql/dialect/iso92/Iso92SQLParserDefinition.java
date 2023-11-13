package com.dbn.language.sql.dialect.iso92;

import com.dbn.language.sql.SQLParserDefinition;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


public class Iso92SQLParserDefinition extends SQLParserDefinition {

    Iso92SQLParserDefinition(Iso92SQLParser parser) {
        super(parser);
    }

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new Iso92SQLParserFlexLexer(getTokenTypes()));
    }

}
