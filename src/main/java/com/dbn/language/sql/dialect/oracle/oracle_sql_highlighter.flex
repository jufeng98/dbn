package com.dbn.language.sql.dialect.oracle;

import com.dbn.language.common.TokenTypeBundle;
import com.dbn.language.common.lexer.DBLanguageLexerBase;
import com.intellij.psi.tree.IElementType;

%%

%class OracleSQLHighlighterFlexLexer
%extends DBLanguageLexerBase
%final
%unicode
%ignorecase
%function advance
%type IElementType
%eof{ return;
%eof}

%{
    public OracleSQLHighlighterFlexLexer(TokenTypeBundle tt) {
        super(tt);
    }
%}


PLSQL_BLOCK_START = "create"({ws}"or"{ws}"replace")? {ws} ("function"|"procedure"|"type"|"trigger"|"package") | "declare" | "begin"
PLSQL_BLOCK_END = ";"{wso}"/"[^*]

%include ../../../common/lexer/shared_elements.flext
%include ../../../common/lexer/shared_elements_oracle.flext
%include ../../../common/lexer/shared_elements_oracle_sql.flext
%include ../../../common/lexer/shared_elements_oracle_psql.flext

VARIABLE = ":"({IDENTIFIER}|{INTEGER})
SQLP_VARIABLE = "&""&"?({IDENTIFIER}|{INTEGER})
VARIABLE_IDENTIFIER={IDENTIFIER}"&""&"?({IDENTIFIER}|{INTEGER})|"<"{IDENTIFIER}({ws}{IDENTIFIER})*">"

%state PSQL_BLOCK
%state NON_PSQL_BLOCK
%%

<YYINITIAL, NON_PSQL_BLOCK> {
    {BLOCK_COMMENT}       { return stt.getBlockComment(); }
    {LINE_COMMENT}        { return stt.getLineComment(); }

    {VARIABLE}            { return stt.getVariable(); }
    {VARIABLE_IDENTIFIER} { return stt.getIdentifier(); }
    {SQLP_VARIABLE}       { return stt.getVariable(); }

    {PLSQL_BLOCK_START}   { yybegin(PSQL_BLOCK); return tt.getKeyword();}

    {INTEGER}             { return tt.getInteger(); }
    {NUMBER}              { return tt.getNumber(); }
    {STRING}              { return tt.getString(); }

    {SQL_FUNCTION}        { return tt.getFunction();}
    {SQL_PARAMETER}       { return tt.getParameter();}
    {SQL_DATA_TYPE}       { return tt.getDataType(); }
    {SQL_KEYWORD}         { return tt.getKeyword(); }

    {OPERATOR}            { return tt.getOperator(); }
    {IDENTIFIER}          { return stt.getIdentifier(); }
    {QUOTED_IDENTIFIER}   { return stt.getQuotedIdentifier(); }

    "("                   { return stt.getChrLeftParenthesis(); }
    ")"                   { return stt.getChrRightParenthesis(); }
    "["                   { return stt.getChrLeftBracket(); }
    "]"                   { return stt.getChrRightBracket(); }

    {WHITE_SPACE}         { return stt.getWhiteSpace(); }
    .                     { return stt.getIdentifier(); }
}

<PSQL_BLOCK> {
    {BLOCK_COMMENT}       { return stt.getBlockComment(); }
    {LINE_COMMENT}        { return stt.getLineComment(); }
//  {VARIABLE}            { return stt.getVariable(); }
    {SQLP_VARIABLE}       { return stt.getVariable(); }

    {PLSQL_BLOCK_END}     { yybegin(YYINITIAL); return stt.getIdentifier(); }

    {INTEGER}             { return tt.getInteger(); }
    {NUMBER}              { return tt.getNumber(); }
    {STRING}              { return tt.getString(); }

    {PLSQL_FUNCTION}      { return tt.getFunction();}
    {PLSQL_PARAMETER}     { return tt.getParameter();}
    {PLSQL_EXCEPTION}     { return tt.getException();}
    {PLSQL_DATA_TYPE}     { return tt.getDataType(); }
    {PLSQL_KEYWORD}       { return tt.getKeyword(); }

    {OPERATOR}            { return tt.getOperator(); }
    {IDENTIFIER}          { return stt.getIdentifier(); }
    {QUOTED_IDENTIFIER}   { return stt.getQuotedIdentifier(); }

    "("                   { return stt.getChrLeftParenthesis(); }
    ")"                   { return stt.getChrRightParenthesis(); }
    "["                   { return stt.getChrLeftBracket(); }
    "]"                   { return stt.getChrRightBracket(); }

    {WHITE_SPACE}         { return stt.getWhiteSpace(); }
    .                     { return stt.getIdentifier(); }
}
