package com.dbn.sql;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.dbn.sql.psi.SqlTypes.*;

%%

%{
  public _SqlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SqlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SELECT=[Ss][Ee][Ll][Ee][Cc][Tt]
DELETE=[Dd][Ee][Ll][Ee][Tt][Ee]
UPDATE=[Uu][Pp][Dd][Aa][Tt][Ee]
INSERT=[Ii][Nn][Ss][Ee][Rr][Tt]
DISTINCT=[Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt]
FROM=[Ff][Rr][Oo][Mm]
EXPLAIN=[Ee][Xx][Pp][Ll][Aa][Ii][Nn]
QUERY=[Qq][Uu][Ee][Rr][Yy]
PLAN=[Pp][Ll][Aa][Nn]
RENAME=[Rr][Ee][Nn][Aa][Mm][Ee]
WHERE=[Ww][Hh][Ee][Rr][Ee]
TO=[Tt][Oo]
ON=[Oo][Nn]
IN=[Ii][Nn]
ADD=[Aa][Dd][Dd]
SET=[Ss][Ee][Tt]
CASE=[Cc][Aa][Ss][Ee]
THEN=[Tt][Hh][Ee][Nn]
ELSE=[Ee][Ll][Ss][Ee]
CAST=[Cc][Aa][Ss][Tt]
LIKE=[Ll][Ii][Kk][Ee]
GLOB=[Gg][Ll][Oo][Bb]
REGEXP=[Rr][Ee][Gg][Ee][Xx][Pp]
VACUUM=[Vv][Aa][Cc][Uu][Uu][Mm]
MATCH=[Mm][Aa][Tt][Cc][Hh]
ESCAPE=[Ee][Ss][Cc][Aa][Pp][Ee]
EXCEPT=[Ee][Xx][Cc][Ee][Pp][Tt]
INTERSECT=[Ii][Nn][Tt][Ee][Rr][Ss][Ee][Cc][Tt]
BETWEEN=[Bb][Ee][Tt][Ww][Ee][Ee][Nn]
NATURAL=[Nn][Aa][Tt][Uu][Rr][Aa][Ll]
END=[Ee][Nn][Dd]
FOR=[Ff][Oo][Rr]
OR=[Oo][Rr]
ABORT=[Aa][Bb][Oo][Rr][Tt]
RAISE=[Rr][Aa][Ii][Ss][Ee]
FAIL=[Ff][Aa][Ii][Ll]
COLUMN=[Cc][Oo][Ll][Uu][Mm][Nn]
IGNORE=[Ii][Gg][Nn][Oo][Rr][Ee]
ALTER=[Aa][Ll][Tt][Ee][Rr]
TABLE=[Tt][Aa][Bb][Ll][Ee]
TEMP=[Tt][Ee][Mm][Pp]
TEMPORARY=[Tt][Ee][Mm][Pp][Oo][Rr][Aa][Rr][Yy]
ANALYZE=[Aa][Nn][Aa][Ll][Yy][Zz][Ee]
ATTACH=[Aa][Tt][Tt][Aa][Cc][Hh]
DATABASE=[Dd][Aa][Tt][Aa][Bb][Aa][Ss][Ee]
AS=[Aa][Ss]
BEGIN=[Bb][Ee][Gg][Ii][Nn]
VIEW=[Vv][Ii][Ee][Ww]
DEFERRED=[Dd][Ee][Ff][Ee][Rr][Rr][Ee][Dd]
IMMEDIATE=[Ii][Mm][Mm][Ee][Dd][Ii][Aa][Tt][Ee]
EXCLUSIVE=[Ee][Xx][Cc][Ll][Uu][Ss][Ii][Vv][Ee]
TRANSACTION=[Tt][Rr][Aa][Nn][Ss][Aa][Cc][Tt][Ii][Oo][Nn]
COMMIT=[Cc][Oo][Mm][Mm][Ii][Tt]
ROLLBACK=[Rr][Oo][Ll][Ll][Bb][Aa][Cc][Kk]
SAVEPOINT=[Ss][Aa][Vv][Ee][Pp][Oo][Ii][Nn][Tt]
RELEASE=[Rr][Ee][Ll][Ee][Aa][Ss][Ee]
CREATE=[Cc][Rr][Ee][Aa][Tt][Ee]
UNIQUE=[Uu][Nn][Ii][Qq][Uu][Ee]
INDEX=[Ii][Nn][Dd][Ee][Xx]
IF=[Ii][Ff]
IS=[Ii][Ss]
OF=[Oo][Ff]
NOT=[Nn][Oo][Tt]
EXISTS=[Ee][Xx][Ii][Ss][Tt][Ss]
RESTRICT=[Rr][Ee][Ss][Tt][Rr][Ii][Cc][Tt]
CASCADE=[Cc][Aa][Ss][Cc][Aa][Dd][Ee]
COLLATE=[Cc][Oo][Ll][Ll][Aa][Tt][Ee]
WITH=[Ww][Ii][Tt][Hh]
LEFT=[Ll][Ee][Ff][Tt]
OUTER=[Oo][Uu][Tt][Ee][Rr]
INNER=[Ii][Nn][Nn][Ee][Rr]
CROSS=[Cc][Rr][Oo][Ss][Ss]
JOIN=[Jj][Oo][Ii][Nn]
USING=[Uu][Ss][Ii][Nn][Gg]
UNION=[Uu][Nn][Ii][Oo][Nn]
ALL=[Aa][Ll][Ll]
AND=[Aa][Nn][Dd]
TRUE=[Tt][Rr][Uu][Ee]
INTO=[Ii][Nn][Tt][Oo]
FALSE=[Ff][Aa][Ll][Ss][Ee]
DROP=[Dd][Rr][Oo][Pp]
VALUES=[Vv][Aa][Ll][Uu][Ee][Ss]
ORDER=[Oo][Rr][Dd][Ee][Rr]
INDEXED=[Ii][Nn][Dd][Ee][Xx][Ee][Dd]
GROUP=[Gg][Rr][Oo][Uu][Pp]
BY=[Bb][Yy]
LIMIT=[Ll][Ii][Mm][Ii][Tt]
OFFSET=[Oo][Ff][Ff][Ss][Ee][Tt]
ASC=[Aa][Ss][Cc]
DESC=[Dd][Ee][Ss][Cc]
WHEN=[Ww][Hh][Ee][Nn]
NULL=[Nn][Uu][Ll][Ll]
WITHOUT=[Ww][Ii][Tt][Hh][Oo][Uu][Tt]
CONSTRAINT=[Cc][On][Nn][Ss][Tt][Rr][Aa][Ii][Nn][Tt]
PRIMARY=[Pp][Rr][Ii][Mm][Aa][Rr][Yy]
KEY=[Kk][Ee][Yy]
CHECK=[Cc][Hh][Ee][Cc][Kk]
DEFAULT=[Dd][Ee][Ff][Aa][Uu][Ll][Tt]
RECURSIVE=[Rr][Ee][Cc][Uu][Rr][Ss][Ii][Vv][Ee]
FOREIGN=[Ff][Oo][Rr][Ee][Ii][Gg][Nn]
REFERENCES_WORD=[Rr][Ee][Ff][Ee][Rr][Ee][Nn][Cc][Ee][Ss]
AUTOINCREMENT=[Aa][Uu][Tt][Oo][Ii][Nn][Cc][Rr][Ee][Mm][Ee][Nn][Tt]
PRAGMA=[Pp][Rr][Aa][Gg][Mm][Aa]
REINDEX=[Rr][Ee][Ii][Nn][Dd][Ee][Xx]
CURRENT_TIME=[Cc][Uu][Rr][Rr][Ee][Nn][Tt]_[Tt][Ii][Mm][Ee]
CURRENT_DATE=[Cc][Uu][Rr][Rr][Ee][Nn][Tt]_[Dd][Aa][Tt][Ee]
CURRENT_TIMESTAMP=[Cc][Uu][Rr][Rr][Ee][Nn][Tt]_[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp]
AUTO_INCREMENT=[Aa][Uu][Tt][Oo]_[Ii][Nn][Cc][Rr][Ee][Mm][Ee][Nn][Tt]
UNSIGNED=[Uu][Nn][Ss][Ii][Gg][Nn][Ee][Dd]
COMMENT_WORD=[Cc][Oo][Mm][Mm][Ee][Nn][Tt]
SEPARATOR=[Ss][Ee][Pp][Aa][Rr][Aa][Tt][Oo][Rr]
INTERVAL=[Ii][Nn][Tt][Ee][Rr][Vv][Aa][Ll]
MONTH=[Mm][Oo][Nn][Tt][Hh]
DAY=[Dd][Aa][Yy]
HOUR=[Hh][Oo][Uu][Rr]
MINUTE=[Mm][Ii][Nn][Uu][Tt][Ee]
CHARACTER=[Cc][Hh][Aa][Rr][Aa][Cc][Tt][Ee][Rr]
SPACE=[ \t\n\x0B\f\r]+
COMMENT=--.*
BLOCK_COMMENT="/"\*([^*]|\*+[^*/])*(\*+"/")?
JAVADOC="/"\*\*(.|\n)*\*"/"
DIGIT=[0-9]+(\.[0-9]*)?
ID=[a-zA-Z0-9_\`\[\]]+
STRING=('([^'])*'?|\"([^\"])*\"?)
MYBATIS_OGNL=[$#]\{[a-zA-Z0-9_@()'.,=\s]+}

%%
<YYINITIAL> {
  {WHITE_SPACE}             { return WHITE_SPACE; }

  ";"                       { return SEMI; }
  "="                       { return EQ; }
  "("                       { return LP; }
  ")"                       { return RP; }
  "."                       { return DOT; }
  ","                       { return COMMA; }
  "+"                       { return PLUS; }
  "-"                       { return MINUS; }
  "~"                       { return BITWISE_NOT; }
  ">>"                      { return SHIFT_RIGHT; }
  "<<"                      { return SHIFT_LEFT; }
  "<"                       { return LT; }
  ">"                       { return GT; }
  "<="                      { return LTE; }
  ">="                      { return GTE; }
  "=="                      { return EQ2; }
  "!="                      { return NEQ; }
  "<>"                      { return NEQ2; }
  "*"                       { return MULTIPLY; }
  "/"                       { return DIVIDE; }
  "%"                       { return MOD; }
  "&"                       { return BITWISE_AND; }
  "|"                       { return BITWISE_OR; }
  "||"                      { return CONCAT; }
  "#"                       { return SHARP; }
  "$"                       { return DOLLAR; }
  "ROWID"                   { return ROWID; }
  "NO"                      { return NO; }
  "ACTION"                  { return ACTION; }
  "DEFERRABLE"              { return DEFERRABLE; }
  "INITIALLY"               { return INITIALLY; }
  "CONFLICT"                { return CONFLICT; }
  "REPLACE"                 { return REPLACE; }
  "TRIGGER"                 { return TRIGGER; }
  "BEFORE"                  { return BEFORE; }
  "AFTER"                   { return AFTER; }
  "INSTEAD"                 { return INSTEAD; }
  "EACH"                    { return EACH; }
  "ROW"                     { return ROW; }
  "VIRTUAL"                 { return VIRTUAL; }
  "DETACH"                  { return DETACH; }
  "E"                       { return E; }
  "HAVING"                  { return HAVING; }

  {SELECT}                  { return SELECT; }
  {DELETE}                  { return DELETE; }
  {UPDATE}                  { return UPDATE; }
  {INSERT}                  { return INSERT; }
  {DISTINCT}                { return DISTINCT; }
  {FROM}                    { return FROM; }
  {EXPLAIN}                 { return EXPLAIN; }
  {QUERY}                   { return QUERY; }
  {PLAN}                    { return PLAN; }
  {RENAME}                  { return RENAME; }
  {WHERE}                   { return WHERE; }
  {TO}                      { return TO; }
  {ON}                      { return ON; }
  {IN}                      { return IN; }
  {ADD}                     { return ADD; }
  {SET}                     { return SET; }
  {CASE}                    { return CASE; }
  {THEN}                    { return THEN; }
  {ELSE}                    { return ELSE; }
  {CAST}                    { return CAST; }
  {LIKE}                    { return LIKE; }
  {GLOB}                    { return GLOB; }
  {REGEXP}                  { return REGEXP; }
  {VACUUM}                  { return VACUUM; }
  {MATCH}                   { return MATCH; }
  {ESCAPE}                  { return ESCAPE; }
  {EXCEPT}                  { return EXCEPT; }
  {INTERSECT}               { return INTERSECT; }
  {BETWEEN}                 { return BETWEEN; }
  {NATURAL}                 { return NATURAL; }
  {END}                     { return END; }
  {FOR}                     { return FOR; }
  {OR}                      { return OR; }
  {ABORT}                   { return ABORT; }
  {RAISE}                   { return RAISE; }
  {FAIL}                    { return FAIL; }
  {COLUMN}                  { return COLUMN; }
  {IGNORE}                  { return IGNORE; }
  {ALTER}                   { return ALTER; }
  {TABLE}                   { return TABLE; }
  {TEMP}                    { return TEMP; }
  {TEMPORARY}               { return TEMPORARY; }
  {ANALYZE}                 { return ANALYZE; }
  {ATTACH}                  { return ATTACH; }
  {DATABASE}                { return DATABASE; }
  {AS}                      { return AS; }
  {BEGIN}                   { return BEGIN; }
  {VIEW}                    { return VIEW; }
  {DEFERRED}                { return DEFERRED; }
  {IMMEDIATE}               { return IMMEDIATE; }
  {EXCLUSIVE}               { return EXCLUSIVE; }
  {TRANSACTION}             { return TRANSACTION; }
  {COMMIT}                  { return COMMIT; }
  {ROLLBACK}                { return ROLLBACK; }
  {SAVEPOINT}               { return SAVEPOINT; }
  {RELEASE}                 { return RELEASE; }
  {CREATE}                  { return CREATE; }
  {UNIQUE}                  { return UNIQUE; }
  {INDEX}                   { return INDEX; }
  {IF}                      { return IF; }
  {IS}                      { return IS; }
  {OF}                      { return OF; }
  {NOT}                     { return NOT; }
  {EXISTS}                  { return EXISTS; }
  {RESTRICT}                { return RESTRICT; }
  {CASCADE}                 { return CASCADE; }
  {COLLATE}                 { return COLLATE; }
  {WITH}                    { return WITH; }
  {LEFT}                    { return LEFT; }
  {OUTER}                   { return OUTER; }
  {INNER}                   { return INNER; }
  {CROSS}                   { return CROSS; }
  {JOIN}                    { return JOIN; }
  {USING}                   { return USING; }
  {UNION}                   { return UNION; }
  {ALL}                     { return ALL; }
  {AND}                     { return AND; }
  {TRUE}                    { return TRUE; }
  {INTO}                    { return INTO; }
  {FALSE}                   { return FALSE; }
  {DROP}                    { return DROP; }
  {VALUES}                  { return VALUES; }
  {ORDER}                   { return ORDER; }
  {INDEXED}                 { return INDEXED; }
  {GROUP}                   { return GROUP; }
  {BY}                      { return BY; }
  {LIMIT}                   { return LIMIT; }
  {OFFSET}                  { return OFFSET; }
  {ASC}                     { return ASC; }
  {DESC}                    { return DESC; }
  {WHEN}                    { return WHEN; }
  {NULL}                    { return NULL; }
  {WITHOUT}                 { return WITHOUT; }
  {CONSTRAINT}              { return CONSTRAINT; }
  {PRIMARY}                 { return PRIMARY; }
  {KEY}                     { return KEY; }
  {CHECK}                   { return CHECK; }
  {DEFAULT}                 { return DEFAULT; }
  {RECURSIVE}               { return RECURSIVE; }
  {FOREIGN}                 { return FOREIGN; }
  {REFERENCES_WORD}         { return REFERENCES_WORD; }
  {AUTOINCREMENT}           { return AUTOINCREMENT; }
  {PRAGMA}                  { return PRAGMA; }
  {REINDEX}                 { return REINDEX; }
  {CURRENT_TIME}            { return CURRENT_TIME; }
  {CURRENT_DATE}            { return CURRENT_DATE; }
  {CURRENT_TIMESTAMP}       { return CURRENT_TIMESTAMP; }
  {AUTO_INCREMENT}          { return AUTO_INCREMENT; }
  {UNSIGNED}                { return UNSIGNED; }
  {COMMENT_WORD}            { return COMMENT_WORD; }
  {SEPARATOR}               { return SEPARATOR; }
  {INTERVAL}                { return INTERVAL; }
  {MONTH}                   { return MONTH; }
  {DAY}                     { return DAY; }
  {HOUR}                    { return HOUR; }
  {MINUTE}                  { return MINUTE; }
  {CHARACTER}               { return CHARACTER; }
  {SPACE}                   { return SPACE; }
  {COMMENT}                 { return COMMENT; }
  {BLOCK_COMMENT}           { return BLOCK_COMMENT; }
  {JAVADOC}                 { return JAVADOC; }
  {DIGIT}                   { return DIGIT; }
  {ID}                      { return ID; }
  {STRING}                  { return STRING; }
  {MYBATIS_OGNL}            { return MYBATIS_OGNL; }

}

[^] { return BAD_CHARACTER; }
