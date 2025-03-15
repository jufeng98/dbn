// This is a generated file. Not intended for manual editing.
package com.dbn.sql.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.dbn.sql.psi.SqlTypes.*;
import static com.dbn.sql.parser.SqlParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SqlParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return sqlFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(BETWEEN_EXPR, BINARY_ADD_EXPR, BINARY_AND_EXPR, BINARY_BITWISE_EXPR,
      BINARY_BOOLEAN_EXPR, BINARY_EQUALITY_EXPR, BINARY_LIKE_EXPR, BINARY_MULT_EXPR,
      BINARY_OR_EXPR, BINARY_PIPE_EXPR, BIND_EXPR, CASE_EXPR,
      CAST_EXPR, COLLATE_EXPR, COLUMN_EXPR, EXISTS_EXPR,
      EXPR, FUNCTION_EXPR, IN_EXPR, IS_EXPR,
      KEYWORD_EXPR, LITERAL_EXPR, MYBATIS_EXPR, NULL_EXPR,
      PAREN_EXPR, RAISE_EXPR, UNARY_EXPR),
  };

  /* ********************************************************** */
  // ALTER TABLE [ database_name '.' ] table_name ( RENAME TO new_table_name | ADD [ COLUMN ] column_def )
  public static boolean alter_table_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt")) return false;
    if (!nextTokenIs(b, ALTER)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ALTER_TABLE_STMT, null);
    r = consumeTokens(b, 1, ALTER, TABLE);
    p = r; // pin = 1
    r = r && report_error_(b, alter_table_stmt_2(b, l + 1));
    r = p && report_error_(b, table_name(b, l + 1)) && r;
    r = p && alter_table_stmt_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ database_name '.' ]
  private static boolean alter_table_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_2")) return false;
    alter_table_stmt_2_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean alter_table_stmt_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // RENAME TO new_table_name | ADD [ COLUMN ] column_def
  private static boolean alter_table_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = alter_table_stmt_4_0(b, l + 1);
    if (!r) r = alter_table_stmt_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // RENAME TO new_table_name
  private static boolean alter_table_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RENAME, TO);
    r = r && new_table_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ADD [ COLUMN ] column_def
  private static boolean alter_table_stmt_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_4_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ADD);
    r = r && alter_table_stmt_4_1_1(b, l + 1);
    r = r && column_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ COLUMN ]
  private static boolean alter_table_stmt_4_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alter_table_stmt_4_1_1")) return false;
    consumeToken(b, COLUMN);
    return true;
  }

  /* ********************************************************** */
  // ANALYZE [ database_name | table_or_index_name | database_name '.' table_or_index_name ]
  public static boolean analyze_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "analyze_stmt")) return false;
    if (!nextTokenIs(b, ANALYZE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ANALYZE_STMT, null);
    r = consumeToken(b, ANALYZE);
    p = r; // pin = 1
    r = r && analyze_stmt_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ database_name | table_or_index_name | database_name '.' table_or_index_name ]
  private static boolean analyze_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "analyze_stmt_1")) return false;
    analyze_stmt_1_0(b, l + 1);
    return true;
  }

  // database_name | table_or_index_name | database_name '.' table_or_index_name
  private static boolean analyze_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "analyze_stmt_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    if (!r) r = table_or_index_name(b, l + 1);
    if (!r) r = analyze_stmt_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // database_name '.' table_or_index_name
  private static boolean analyze_stmt_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "analyze_stmt_1_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    r = r && table_or_index_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ATTACH [ DATABASE ] expr AS database_name
  public static boolean attach_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attach_stmt")) return false;
    if (!nextTokenIs(b, ATTACH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATTACH_STMT, null);
    r = consumeToken(b, ATTACH);
    p = r; // pin = 1
    r = r && report_error_(b, attach_stmt_1(b, l + 1));
    r = p && report_error_(b, expr(b, l + 1, -1)) && r;
    r = p && report_error_(b, consumeToken(b, AS)) && r;
    r = p && database_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ DATABASE ]
  private static boolean attach_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attach_stmt_1")) return false;
    consumeToken(b, DATABASE);
    return true;
  }

  /* ********************************************************** */
  // id | AUTO_INCREMENT | DEFAULT | COMMENT_WORD | COLLATE | ENGINE | CHARSET
  public static boolean attr_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attr_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTR_NAME, "<attr name>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, AUTO_INCREMENT);
    if (!r) r = consumeToken(b, DEFAULT);
    if (!r) r = consumeToken(b, COMMENT_WORD);
    if (!r) r = consumeToken(b, COLLATE);
    if (!r) r = consumeToken(b, ENGINE);
    if (!r) r = consumeToken(b, CHARSET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // id | string | digit
  public static boolean attr_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attr_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTR_VALUE, "<attr value>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, DIGIT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BEGIN [ DEFERRED | IMMEDIATE | EXCLUSIVE ] [ TRANSACTION ]
  public static boolean begin_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "begin_stmt")) return false;
    if (!nextTokenIs(b, BEGIN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BEGIN_STMT, null);
    r = consumeToken(b, BEGIN);
    p = r; // pin = 1
    r = r && report_error_(b, begin_stmt_1(b, l + 1));
    r = p && begin_stmt_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ DEFERRED | IMMEDIATE | EXCLUSIVE ]
  private static boolean begin_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "begin_stmt_1")) return false;
    begin_stmt_1_0(b, l + 1);
    return true;
  }

  // DEFERRED | IMMEDIATE | EXCLUSIVE
  private static boolean begin_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "begin_stmt_1_0")) return false;
    boolean r;
    r = consumeToken(b, DEFERRED);
    if (!r) r = consumeToken(b, IMMEDIATE);
    if (!r) r = consumeToken(b, EXCLUSIVE);
    return r;
  }

  // [ TRANSACTION ]
  private static boolean begin_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "begin_stmt_2")) return false;
    consumeToken(b, TRANSACTION);
    return true;
  }

  /* ********************************************************** */
  // LIKE | GLOB | REGEXP | MATCH
  public static boolean binary_like_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BINARY_LIKE_OPERATOR, "<binary like operator>");
    r = consumeToken(b, LIKE);
    if (!r) r = consumeToken(b, GLOB);
    if (!r) r = consumeToken(b, REGEXP);
    if (!r) r = consumeToken(b, MATCH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '?' [digit] | ':' identifier
  public static boolean bind_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bind_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BIND_PARAMETER, "<bind parameter>");
    r = bind_parameter_0(b, l + 1);
    if (!r) r = bind_parameter_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '?' [digit]
  private static boolean bind_parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bind_parameter_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "?");
    r = r && bind_parameter_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [digit]
  private static boolean bind_parameter_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bind_parameter_0_1")) return false;
    consumeToken(b, DIGIT);
    return true;
  }

  // ':' identifier
  private static boolean bind_parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bind_parameter_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ":");
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '0x' digit
  public static boolean blob_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blob_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOB_LITERAL, "<blob literal>");
    r = consumeToken(b, "0x");
    r = r && consumeToken(b, DIGIT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean collation_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "collation_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, COLLATION_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean column_alias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_alias")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, COLUMN_ALIAS, r);
    return r;
  }

  /* ********************************************************** */
  // [ CONSTRAINT identifier ] ( PRIMARY KEY [ ASC | DESC ] conflict_clause [ AUTOINCREMENT ] |
  //     UNSIGNED | NOT NULL [conflict_clause] | UNIQUE conflict_clause | CHECK '(' expr ')' | AUTO_INCREMENT | CHARACTER SET identifier |
  //     DEFAULT ( signed_number | literal_value | '(' expr ')' ) | COLLATE collation_name | foreign_key_clause | COMMENT_WORD string )
  public static boolean column_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLUMN_CONSTRAINT, "<column constraint>");
    r = column_constraint_0(b, l + 1);
    r = r && column_constraint_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ CONSTRAINT identifier ]
  private static boolean column_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_0")) return false;
    column_constraint_0_0(b, l + 1);
    return true;
  }

  // CONSTRAINT identifier
  private static boolean column_constraint_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONSTRAINT);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PRIMARY KEY [ ASC | DESC ] conflict_clause [ AUTOINCREMENT ] |
  //     UNSIGNED | NOT NULL [conflict_clause] | UNIQUE conflict_clause | CHECK '(' expr ')' | AUTO_INCREMENT | CHARACTER SET identifier |
  //     DEFAULT ( signed_number | literal_value | '(' expr ')' ) | COLLATE collation_name | foreign_key_clause | COMMENT_WORD string
  private static boolean column_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = column_constraint_1_0(b, l + 1);
    if (!r) r = consumeToken(b, UNSIGNED);
    if (!r) r = column_constraint_1_2(b, l + 1);
    if (!r) r = column_constraint_1_3(b, l + 1);
    if (!r) r = column_constraint_1_4(b, l + 1);
    if (!r) r = consumeToken(b, AUTO_INCREMENT);
    if (!r) r = column_constraint_1_6(b, l + 1);
    if (!r) r = column_constraint_1_7(b, l + 1);
    if (!r) r = column_constraint_1_8(b, l + 1);
    if (!r) r = foreign_key_clause(b, l + 1);
    if (!r) r = parseTokens(b, 0, COMMENT_WORD, STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  // PRIMARY KEY [ ASC | DESC ] conflict_clause [ AUTOINCREMENT ]
  private static boolean column_constraint_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PRIMARY, KEY);
    r = r && column_constraint_1_0_2(b, l + 1);
    r = r && conflict_clause(b, l + 1);
    r = r && column_constraint_1_0_4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ASC | DESC ]
  private static boolean column_constraint_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_0_2")) return false;
    column_constraint_1_0_2_0(b, l + 1);
    return true;
  }

  // ASC | DESC
  private static boolean column_constraint_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_0_2_0")) return false;
    boolean r;
    r = consumeToken(b, ASC);
    if (!r) r = consumeToken(b, DESC);
    return r;
  }

  // [ AUTOINCREMENT ]
  private static boolean column_constraint_1_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_0_4")) return false;
    consumeToken(b, AUTOINCREMENT);
    return true;
  }

  // NOT NULL [conflict_clause]
  private static boolean column_constraint_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NOT, NULL);
    r = r && column_constraint_1_2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [conflict_clause]
  private static boolean column_constraint_1_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_2_2")) return false;
    conflict_clause(b, l + 1);
    return true;
  }

  // UNIQUE conflict_clause
  private static boolean column_constraint_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNIQUE);
    r = r && conflict_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CHECK '(' expr ')'
  private static boolean column_constraint_1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CHECK, LP);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // CHARACTER SET identifier
  private static boolean column_constraint_1_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CHARACTER, SET);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DEFAULT ( signed_number | literal_value | '(' expr ')' )
  private static boolean column_constraint_1_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DEFAULT);
    r = r && column_constraint_1_7_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // signed_number | literal_value | '(' expr ')'
  private static boolean column_constraint_1_7_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_7_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = signed_number(b, l + 1);
    if (!r) r = literal_value(b, l + 1);
    if (!r) r = column_constraint_1_7_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' expr ')'
  private static boolean column_constraint_1_7_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_7_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLLATE collation_name
  private static boolean column_constraint_1_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_constraint_1_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLLATE);
    r = r && collation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // column_name type_name ( column_constraint ) *
  public static boolean column_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_def")) return false;
    if (!nextTokenIs(b, "<column def>", ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLUMN_DEF, "<column def>");
    r = column_name(b, l + 1);
    r = r && type_name(b, l + 1);
    r = r && column_def_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( column_constraint ) *
  private static boolean column_def_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_def_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!column_def_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "column_def_2", c)) break;
    }
    return true;
  }

  // ( column_constraint )
  private static boolean column_def_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_def_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = column_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id | string
  public static boolean column_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_name")) return false;
    if (!nextTokenIs(b, "<column name>", ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLUMN_NAME, "<column name>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ( COMMIT | END ) [ TRANSACTION ]
  public static boolean commit_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commit_stmt")) return false;
    if (!nextTokenIs(b, "<commit stmt>", COMMIT, END)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, COMMIT_STMT, "<commit stmt>");
    r = commit_stmt_0(b, l + 1);
    p = r; // pin = 1
    r = r && commit_stmt_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // COMMIT | END
  private static boolean commit_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commit_stmt_0")) return false;
    boolean r;
    r = consumeToken(b, COMMIT);
    if (!r) r = consumeToken(b, END);
    return r;
  }

  // [ TRANSACTION ]
  private static boolean commit_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "commit_stmt_1")) return false;
    consumeToken(b, TRANSACTION);
    return true;
  }

  /* ********************************************************** */
  // UNION ALL | UNION | INTERSECT | EXCEPT
  public static boolean compound_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPOUND_OPERATOR, "<compound operator>");
    r = parseTokens(b, 0, UNION, ALL);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, INTERSECT);
    if (!r) r = consumeToken(b, EXCEPT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // result_column ( ',' result_column )*
  public static boolean compound_result_column(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_result_column")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPOUND_RESULT_COLUMN, "<compound result column>");
    r = result_column(b, l + 1);
    r = r && compound_result_column_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' result_column )*
  private static boolean compound_result_column_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_result_column_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!compound_result_column_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "compound_result_column_1", c)) break;
    }
    return true;
  }

  // ',' result_column
  private static boolean compound_result_column_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_result_column_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && result_column(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] select_stmt  ( compound_operator select_stmt ) *
  //     [ ORDER BY ordering_term ( ',' ordering_term ) * ] [ LIMIT limiting_term [ ( OFFSET | ',' ) limiting_term ] ]
  public static boolean compound_select_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMPOUND_SELECT_STMT, "<compound select stmt>");
    r = compound_select_stmt_0(b, l + 1);
    r = r && select_stmt(b, l + 1);
    r = r && compound_select_stmt_2(b, l + 1);
    r = r && compound_select_stmt_3(b, l + 1);
    r = r && compound_select_stmt_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ with_clause ]
  private static boolean compound_select_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // ( compound_operator select_stmt ) *
  private static boolean compound_select_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!compound_select_stmt_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "compound_select_stmt_2", c)) break;
    }
    return true;
  }

  // compound_operator select_stmt
  private static boolean compound_select_stmt_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compound_operator(b, l + 1);
    r = r && select_stmt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ]
  private static boolean compound_select_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_3")) return false;
    compound_select_stmt_3_0(b, l + 1);
    return true;
  }

  // ORDER BY ordering_term ( ',' ordering_term ) *
  private static boolean compound_select_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ORDER, BY);
    r = r && ordering_term(b, l + 1);
    r = r && compound_select_stmt_3_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' ordering_term ) *
  private static boolean compound_select_stmt_3_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_3_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!compound_select_stmt_3_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "compound_select_stmt_3_0_3", c)) break;
    }
    return true;
  }

  // ',' ordering_term
  private static boolean compound_select_stmt_3_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_3_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ordering_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ LIMIT limiting_term [ ( OFFSET | ',' ) limiting_term ] ]
  private static boolean compound_select_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_4")) return false;
    compound_select_stmt_4_0(b, l + 1);
    return true;
  }

  // LIMIT limiting_term [ ( OFFSET | ',' ) limiting_term ]
  private static boolean compound_select_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LIMIT);
    r = r && limiting_term(b, l + 1);
    r = r && compound_select_stmt_4_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ( OFFSET | ',' ) limiting_term ]
  private static boolean compound_select_stmt_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_4_0_2")) return false;
    compound_select_stmt_4_0_2_0(b, l + 1);
    return true;
  }

  // ( OFFSET | ',' ) limiting_term
  private static boolean compound_select_stmt_4_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_4_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compound_select_stmt_4_0_2_0_0(b, l + 1);
    r = r && limiting_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OFFSET | ','
  private static boolean compound_select_stmt_4_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_select_stmt_4_0_2_0_0")) return false;
    boolean r;
    r = consumeToken(b, OFFSET);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // [ ON CONFLICT ( ROLLBACK | ABORT | FAIL | IGNORE | REPLACE ) | USING identifier ]
  public static boolean conflict_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conflict_clause")) return false;
    Marker m = enter_section_(b, l, _NONE_, CONFLICT_CLAUSE, "<conflict clause>");
    conflict_clause_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // ON CONFLICT ( ROLLBACK | ABORT | FAIL | IGNORE | REPLACE ) | USING identifier
  private static boolean conflict_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conflict_clause_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conflict_clause_0_0(b, l + 1);
    if (!r) r = conflict_clause_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ON CONFLICT ( ROLLBACK | ABORT | FAIL | IGNORE | REPLACE )
  private static boolean conflict_clause_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conflict_clause_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ON, CONFLICT);
    r = r && conflict_clause_0_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ROLLBACK | ABORT | FAIL | IGNORE | REPLACE
  private static boolean conflict_clause_0_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conflict_clause_0_0_2")) return false;
    boolean r;
    r = consumeToken(b, ROLLBACK);
    if (!r) r = consumeToken(b, ABORT);
    if (!r) r = consumeToken(b, FAIL);
    if (!r) r = consumeToken(b, IGNORE);
    if (!r) r = consumeToken(b, REPLACE);
    return r;
  }

  // USING identifier
  private static boolean conflict_clause_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conflict_clause_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, USING);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CREATE [ UNIQUE ] INDEX [ IF NOT EXISTS ] [ database_name '.' ] index_name
  //     ON table_name '(' indexed_column ( ',' indexed_column ) * ')' [ WHERE expr ]
  public static boolean create_index_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt")) return false;
    if (!nextTokenIs(b, CREATE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CREATE_INDEX_STMT, null);
    r = consumeToken(b, CREATE);
    r = r && create_index_stmt_1(b, l + 1);
    r = r && consumeToken(b, INDEX);
    r = r && create_index_stmt_3(b, l + 1);
    r = r && create_index_stmt_4(b, l + 1);
    r = r && index_name(b, l + 1);
    p = r; // pin = 6
    r = r && report_error_(b, consumeToken(b, ON));
    r = p && report_error_(b, table_name(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, indexed_column(b, l + 1)) && r;
    r = p && report_error_(b, create_index_stmt_10(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RP)) && r;
    r = p && create_index_stmt_12(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ UNIQUE ]
  private static boolean create_index_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_1")) return false;
    consumeToken(b, UNIQUE);
    return true;
  }

  // [ IF NOT EXISTS ]
  private static boolean create_index_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_3")) return false;
    parseTokens(b, 0, IF, NOT, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean create_index_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_4")) return false;
    create_index_stmt_4_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean create_index_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' indexed_column ) *
  private static boolean create_index_stmt_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_10")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_index_stmt_10_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_index_stmt_10", c)) break;
    }
    return true;
  }

  // ',' indexed_column
  private static boolean create_index_stmt_10_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_10_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && indexed_column(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ WHERE expr ]
  private static boolean create_index_stmt_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_12")) return false;
    create_index_stmt_12_0(b, l + 1);
    return true;
  }

  // WHERE expr
  private static boolean create_index_stmt_12_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_index_stmt_12_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHERE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CREATE [ TEMP | TEMPORARY ] TABLE [ IF NOT EXISTS ] [ database_name '.' ] table_name
  //     ( '(' column_def ( ',' column_def ) * ( ',' table_constraint ) * ')' table_attr * [ WITHOUT ROWID ] | AS compound_select_stmt )
  public static boolean create_table_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt")) return false;
    if (!nextTokenIs(b, CREATE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CREATE_TABLE_STMT, null);
    r = consumeToken(b, CREATE);
    r = r && create_table_stmt_1(b, l + 1);
    r = r && consumeToken(b, TABLE);
    r = r && create_table_stmt_3(b, l + 1);
    r = r && create_table_stmt_4(b, l + 1);
    r = r && table_name(b, l + 1);
    p = r; // pin = 6
    r = r && create_table_stmt_6(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ TEMP | TEMPORARY ]
  private static boolean create_table_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_1")) return false;
    create_table_stmt_1_0(b, l + 1);
    return true;
  }

  // TEMP | TEMPORARY
  private static boolean create_table_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_1_0")) return false;
    boolean r;
    r = consumeToken(b, TEMP);
    if (!r) r = consumeToken(b, TEMPORARY);
    return r;
  }

  // [ IF NOT EXISTS ]
  private static boolean create_table_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_3")) return false;
    parseTokens(b, 0, IF, NOT, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean create_table_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_4")) return false;
    create_table_stmt_4_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean create_table_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' column_def ( ',' column_def ) * ( ',' table_constraint ) * ')' table_attr * [ WITHOUT ROWID ] | AS compound_select_stmt
  private static boolean create_table_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = create_table_stmt_6_0(b, l + 1);
    if (!r) r = create_table_stmt_6_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' column_def ( ',' column_def ) * ( ',' table_constraint ) * ')' table_attr * [ WITHOUT ROWID ]
  private static boolean create_table_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && column_def(b, l + 1);
    r = r && create_table_stmt_6_0_2(b, l + 1);
    r = r && create_table_stmt_6_0_3(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && create_table_stmt_6_0_5(b, l + 1);
    r = r && create_table_stmt_6_0_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_def ) *
  private static boolean create_table_stmt_6_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_table_stmt_6_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_table_stmt_6_0_2", c)) break;
    }
    return true;
  }

  // ',' column_def
  private static boolean create_table_stmt_6_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' table_constraint ) *
  private static boolean create_table_stmt_6_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_table_stmt_6_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_table_stmt_6_0_3", c)) break;
    }
    return true;
  }

  // ',' table_constraint
  private static boolean create_table_stmt_6_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && table_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_attr *
  private static boolean create_table_stmt_6_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_attr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_table_stmt_6_0_5", c)) break;
    }
    return true;
  }

  // [ WITHOUT ROWID ]
  private static boolean create_table_stmt_6_0_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_0_6")) return false;
    parseTokens(b, 0, WITHOUT, ROWID);
    return true;
  }

  // AS compound_select_stmt
  private static boolean create_table_stmt_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_table_stmt_6_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AS);
    r = r && compound_select_stmt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CREATE [ TEMP | TEMPORARY ] TRIGGER [ IF NOT EXISTS ] [ database_name '.' ] trigger_name
  //     [ BEFORE | AFTER | INSTEAD OF ] ( DELETE | INSERT | UPDATE [ OF column_name ( ',' column_name ) * ] )
  //     ON table_name [ FOR EACH ROW] [ WHEN expr ] BEGIN ( (update_stmt | insert_stmt | delete_stmt | compound_select_stmt ) ';' ) + END
  public static boolean create_trigger_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt")) return false;
    if (!nextTokenIs(b, CREATE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CREATE_TRIGGER_STMT, null);
    r = consumeToken(b, CREATE);
    r = r && create_trigger_stmt_1(b, l + 1);
    r = r && consumeToken(b, TRIGGER);
    r = r && create_trigger_stmt_3(b, l + 1);
    r = r && create_trigger_stmt_4(b, l + 1);
    r = r && trigger_name(b, l + 1);
    p = r; // pin = 6
    r = r && report_error_(b, create_trigger_stmt_6(b, l + 1));
    r = p && report_error_(b, create_trigger_stmt_7(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, ON)) && r;
    r = p && report_error_(b, table_name(b, l + 1)) && r;
    r = p && report_error_(b, create_trigger_stmt_10(b, l + 1)) && r;
    r = p && report_error_(b, create_trigger_stmt_11(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, BEGIN)) && r;
    r = p && report_error_(b, create_trigger_stmt_13(b, l + 1)) && r;
    r = p && consumeToken(b, END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ TEMP | TEMPORARY ]
  private static boolean create_trigger_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_1")) return false;
    create_trigger_stmt_1_0(b, l + 1);
    return true;
  }

  // TEMP | TEMPORARY
  private static boolean create_trigger_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_1_0")) return false;
    boolean r;
    r = consumeToken(b, TEMP);
    if (!r) r = consumeToken(b, TEMPORARY);
    return r;
  }

  // [ IF NOT EXISTS ]
  private static boolean create_trigger_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_3")) return false;
    parseTokens(b, 0, IF, NOT, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean create_trigger_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_4")) return false;
    create_trigger_stmt_4_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean create_trigger_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ BEFORE | AFTER | INSTEAD OF ]
  private static boolean create_trigger_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_6")) return false;
    create_trigger_stmt_6_0(b, l + 1);
    return true;
  }

  // BEFORE | AFTER | INSTEAD OF
  private static boolean create_trigger_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BEFORE);
    if (!r) r = consumeToken(b, AFTER);
    if (!r) r = parseTokens(b, 0, INSTEAD, OF);
    exit_section_(b, m, null, r);
    return r;
  }

  // DELETE | INSERT | UPDATE [ OF column_name ( ',' column_name ) * ]
  private static boolean create_trigger_stmt_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DELETE);
    if (!r) r = consumeToken(b, INSERT);
    if (!r) r = create_trigger_stmt_7_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // UPDATE [ OF column_name ( ',' column_name ) * ]
  private static boolean create_trigger_stmt_7_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UPDATE);
    r = r && create_trigger_stmt_7_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OF column_name ( ',' column_name ) * ]
  private static boolean create_trigger_stmt_7_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7_2_1")) return false;
    create_trigger_stmt_7_2_1_0(b, l + 1);
    return true;
  }

  // OF column_name ( ',' column_name ) *
  private static boolean create_trigger_stmt_7_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OF);
    r = r && column_name(b, l + 1);
    r = r && create_trigger_stmt_7_2_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_name ) *
  private static boolean create_trigger_stmt_7_2_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7_2_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_trigger_stmt_7_2_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_trigger_stmt_7_2_1_0_2", c)) break;
    }
    return true;
  }

  // ',' column_name
  private static boolean create_trigger_stmt_7_2_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_7_2_1_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ FOR EACH ROW]
  private static boolean create_trigger_stmt_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_10")) return false;
    parseTokens(b, 0, FOR, EACH, ROW);
    return true;
  }

  // [ WHEN expr ]
  private static boolean create_trigger_stmt_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_11")) return false;
    create_trigger_stmt_11_0(b, l + 1);
    return true;
  }

  // WHEN expr
  private static boolean create_trigger_stmt_11_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_11_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHEN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( (update_stmt | insert_stmt | delete_stmt | compound_select_stmt ) ';' ) +
  private static boolean create_trigger_stmt_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_13")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = create_trigger_stmt_13_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!create_trigger_stmt_13_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_trigger_stmt_13", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (update_stmt | insert_stmt | delete_stmt | compound_select_stmt ) ';'
  private static boolean create_trigger_stmt_13_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_13_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = create_trigger_stmt_13_0_0(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  // update_stmt | insert_stmt | delete_stmt | compound_select_stmt
  private static boolean create_trigger_stmt_13_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_trigger_stmt_13_0_0")) return false;
    boolean r;
    r = update_stmt(b, l + 1);
    if (!r) r = insert_stmt(b, l + 1);
    if (!r) r = delete_stmt(b, l + 1);
    if (!r) r = compound_select_stmt(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // CREATE [ TEMP | TEMPORARY ] VIEW [ IF NOT EXISTS ] [ database_name '.' ] view_name
  //     [ '(' column_alias ( ',' column_alias ) * ')' ] AS compound_select_stmt
  public static boolean create_view_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt")) return false;
    if (!nextTokenIs(b, CREATE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CREATE_VIEW_STMT, null);
    r = consumeToken(b, CREATE);
    r = r && create_view_stmt_1(b, l + 1);
    r = r && consumeToken(b, VIEW);
    r = r && create_view_stmt_3(b, l + 1);
    r = r && create_view_stmt_4(b, l + 1);
    r = r && view_name(b, l + 1);
    p = r; // pin = 6
    r = r && report_error_(b, create_view_stmt_6(b, l + 1));
    r = p && report_error_(b, consumeToken(b, AS)) && r;
    r = p && compound_select_stmt(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ TEMP | TEMPORARY ]
  private static boolean create_view_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_1")) return false;
    create_view_stmt_1_0(b, l + 1);
    return true;
  }

  // TEMP | TEMPORARY
  private static boolean create_view_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_1_0")) return false;
    boolean r;
    r = consumeToken(b, TEMP);
    if (!r) r = consumeToken(b, TEMPORARY);
    return r;
  }

  // [ IF NOT EXISTS ]
  private static boolean create_view_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_3")) return false;
    parseTokens(b, 0, IF, NOT, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean create_view_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_4")) return false;
    create_view_stmt_4_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean create_view_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '(' column_alias ( ',' column_alias ) * ')' ]
  private static boolean create_view_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_6")) return false;
    create_view_stmt_6_0(b, l + 1);
    return true;
  }

  // '(' column_alias ( ',' column_alias ) * ')'
  private static boolean create_view_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && column_alias(b, l + 1);
    r = r && create_view_stmt_6_0_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_alias ) *
  private static boolean create_view_stmt_6_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_6_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_view_stmt_6_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_view_stmt_6_0_2", c)) break;
    }
    return true;
  }

  // ',' column_alias
  private static boolean create_view_stmt_6_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_view_stmt_6_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CREATE VIRTUAL TABLE [ IF NOT EXISTS ] [ database_name '.' ] table_name
  //     USING module_name [ '(' <<custom_module_argument column_def>> ( ',' <<custom_module_argument column_def>> ) * ')' ]
  public static boolean create_virtual_table_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt")) return false;
    if (!nextTokenIs(b, CREATE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CREATE_VIRTUAL_TABLE_STMT, null);
    r = consumeTokens(b, 0, CREATE, VIRTUAL, TABLE);
    r = r && create_virtual_table_stmt_3(b, l + 1);
    r = r && create_virtual_table_stmt_4(b, l + 1);
    r = r && table_name(b, l + 1);
    p = r; // pin = 6
    r = r && report_error_(b, consumeToken(b, USING));
    r = p && report_error_(b, module_name(b, l + 1)) && r;
    r = p && create_virtual_table_stmt_8(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ IF NOT EXISTS ]
  private static boolean create_virtual_table_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_3")) return false;
    parseTokens(b, 0, IF, NOT, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean create_virtual_table_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_4")) return false;
    create_virtual_table_stmt_4_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean create_virtual_table_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '(' <<custom_module_argument column_def>> ( ',' <<custom_module_argument column_def>> ) * ')' ]
  private static boolean create_virtual_table_stmt_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_8")) return false;
    create_virtual_table_stmt_8_0(b, l + 1);
    return true;
  }

  // '(' <<custom_module_argument column_def>> ( ',' <<custom_module_argument column_def>> ) * ')'
  private static boolean create_virtual_table_stmt_8_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_8_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && custom_module_argument(b, l + 1, SqlParser::column_def);
    r = r && create_virtual_table_stmt_8_0_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' <<custom_module_argument column_def>> ) *
  private static boolean create_virtual_table_stmt_8_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_8_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!create_virtual_table_stmt_8_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "create_virtual_table_stmt_8_0_2", c)) break;
    }
    return true;
  }

  // ',' <<custom_module_argument column_def>>
  private static boolean create_virtual_table_stmt_8_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "create_virtual_table_stmt_8_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && custom_module_argument(b, l + 1, SqlParser::column_def);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // table_name [ '(' column_alias ( ',' column_alias ) * ')' ]
  public static boolean cte_table_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cte_table_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_name(b, l + 1);
    r = r && cte_table_name_1(b, l + 1);
    exit_section_(b, m, CTE_TABLE_NAME, r);
    return r;
  }

  // [ '(' column_alias ( ',' column_alias ) * ')' ]
  private static boolean cte_table_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cte_table_name_1")) return false;
    cte_table_name_1_0(b, l + 1);
    return true;
  }

  // '(' column_alias ( ',' column_alias ) * ')'
  private static boolean cte_table_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cte_table_name_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && column_alias(b, l + 1);
    r = r && cte_table_name_1_0_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_alias ) *
  private static boolean cte_table_name_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cte_table_name_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!cte_table_name_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "cte_table_name_1_0_2", c)) break;
    }
    return true;
  }

  // ',' column_alias
  private static boolean cte_table_name_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cte_table_name_1_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean database_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "database_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, DATABASE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] DELETE FROM qualified_table_name [ WHERE expr ]
  public static boolean delete_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt")) return false;
    if (!nextTokenIs(b, "<delete stmt>", DELETE, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DELETE_STMT, "<delete stmt>");
    r = delete_stmt_0(b, l + 1);
    r = r && consumeTokens(b, 0, DELETE, FROM);
    r = r && qualified_table_name(b, l + 1);
    p = r; // pin = 4
    r = r && delete_stmt_4(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ with_clause ]
  private static boolean delete_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // [ WHERE expr ]
  private static boolean delete_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_4")) return false;
    delete_stmt_4_0(b, l + 1);
    return true;
  }

  // WHERE expr
  private static boolean delete_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHERE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] DELETE FROM qualified_table_name [ WHERE expr ]
  //     [ [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ] ]
  public static boolean delete_stmt_limited(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited")) return false;
    if (!nextTokenIs(b, "<delete stmt limited>", DELETE, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DELETE_STMT_LIMITED, "<delete stmt limited>");
    r = delete_stmt_limited_0(b, l + 1);
    r = r && consumeTokens(b, 0, DELETE, FROM);
    r = r && qualified_table_name(b, l + 1);
    p = r; // pin = 4
    r = r && report_error_(b, delete_stmt_limited_4(b, l + 1));
    r = p && delete_stmt_limited_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ with_clause ]
  private static boolean delete_stmt_limited_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // [ WHERE expr ]
  private static boolean delete_stmt_limited_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_4")) return false;
    delete_stmt_limited_4_0(b, l + 1);
    return true;
  }

  // WHERE expr
  private static boolean delete_stmt_limited_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHERE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ] ]
  private static boolean delete_stmt_limited_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5")) return false;
    delete_stmt_limited_5_0(b, l + 1);
    return true;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ]
  private static boolean delete_stmt_limited_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = delete_stmt_limited_5_0_0(b, l + 1);
    r = r && consumeToken(b, LIMIT);
    r = r && expr(b, l + 1, -1);
    r = r && delete_stmt_limited_5_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ]
  private static boolean delete_stmt_limited_5_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_0")) return false;
    delete_stmt_limited_5_0_0_0(b, l + 1);
    return true;
  }

  // ORDER BY ordering_term ( ',' ordering_term ) *
  private static boolean delete_stmt_limited_5_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ORDER, BY);
    r = r && ordering_term(b, l + 1);
    r = r && delete_stmt_limited_5_0_0_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' ordering_term ) *
  private static boolean delete_stmt_limited_5_0_0_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_0_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!delete_stmt_limited_5_0_0_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "delete_stmt_limited_5_0_0_0_3", c)) break;
    }
    return true;
  }

  // ',' ordering_term
  private static boolean delete_stmt_limited_5_0_0_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_0_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ordering_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ( OFFSET | ',' ) expr ]
  private static boolean delete_stmt_limited_5_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_3")) return false;
    delete_stmt_limited_5_0_3_0(b, l + 1);
    return true;
  }

  // ( OFFSET | ',' ) expr
  private static boolean delete_stmt_limited_5_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = delete_stmt_limited_5_0_3_0_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OFFSET | ','
  private static boolean delete_stmt_limited_5_0_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "delete_stmt_limited_5_0_3_0_0")) return false;
    boolean r;
    r = consumeToken(b, OFFSET);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // DETACH [ DATABASE ] database_name
  public static boolean detach_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "detach_stmt")) return false;
    if (!nextTokenIs(b, DETACH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DETACH_STMT, null);
    r = consumeToken(b, DETACH);
    p = r; // pin = 1
    r = r && report_error_(b, detach_stmt_1(b, l + 1));
    r = p && database_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ DATABASE ]
  private static boolean detach_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "detach_stmt_1")) return false;
    consumeToken(b, DATABASE);
    return true;
  }

  /* ********************************************************** */
  // DROP INDEX [ IF EXISTS ] [ database_name '.' ] index_name
  public static boolean drop_index_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_index_stmt")) return false;
    if (!nextTokenIs(b, DROP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DROP_INDEX_STMT, null);
    r = consumeTokens(b, 2, DROP, INDEX);
    p = r; // pin = 2
    r = r && report_error_(b, drop_index_stmt_2(b, l + 1));
    r = p && report_error_(b, drop_index_stmt_3(b, l + 1)) && r;
    r = p && index_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ IF EXISTS ]
  private static boolean drop_index_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_index_stmt_2")) return false;
    parseTokens(b, 0, IF, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean drop_index_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_index_stmt_3")) return false;
    drop_index_stmt_3_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean drop_index_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_index_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DROP TABLE [ IF EXISTS ] [ database_name '.' ] table_name
  public static boolean drop_table_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_table_stmt")) return false;
    if (!nextTokenIs(b, DROP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DROP_TABLE_STMT, null);
    r = consumeTokens(b, 2, DROP, TABLE);
    p = r; // pin = 2
    r = r && report_error_(b, drop_table_stmt_2(b, l + 1));
    r = p && report_error_(b, drop_table_stmt_3(b, l + 1)) && r;
    r = p && table_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ IF EXISTS ]
  private static boolean drop_table_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_table_stmt_2")) return false;
    parseTokens(b, 0, IF, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean drop_table_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_table_stmt_3")) return false;
    drop_table_stmt_3_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean drop_table_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_table_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DROP TRIGGER [ IF EXISTS ] [ database_name '.' ] trigger_name
  public static boolean drop_trigger_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_trigger_stmt")) return false;
    if (!nextTokenIs(b, DROP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DROP_TRIGGER_STMT, null);
    r = consumeTokens(b, 2, DROP, TRIGGER);
    p = r; // pin = 2
    r = r && report_error_(b, drop_trigger_stmt_2(b, l + 1));
    r = p && report_error_(b, drop_trigger_stmt_3(b, l + 1)) && r;
    r = p && trigger_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ IF EXISTS ]
  private static boolean drop_trigger_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_trigger_stmt_2")) return false;
    parseTokens(b, 0, IF, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean drop_trigger_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_trigger_stmt_3")) return false;
    drop_trigger_stmt_3_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean drop_trigger_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_trigger_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DROP VIEW [ IF EXISTS ] [ database_name '.' ] view_name
  public static boolean drop_view_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_view_stmt")) return false;
    if (!nextTokenIs(b, DROP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DROP_VIEW_STMT, null);
    r = consumeTokens(b, 2, DROP, VIEW);
    p = r; // pin = 2
    r = r && report_error_(b, drop_view_stmt_2(b, l + 1));
    r = p && report_error_(b, drop_view_stmt_3(b, l + 1)) && r;
    r = p && view_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ IF EXISTS ]
  private static boolean drop_view_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_view_stmt_2")) return false;
    parseTokens(b, 0, IF, EXISTS);
    return true;
  }

  // [ database_name '.' ]
  private static boolean drop_view_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_view_stmt_3")) return false;
    drop_view_stmt_3_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean drop_view_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "drop_view_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string
  public static boolean error_message(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "error_message")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, ERROR_MESSAGE, r);
    return r;
  }

  /* ********************************************************** */
  // REFERENCES_WORD foreign_table [ '(' column_name ( ',' column_name ) * ')' ]
  //     [ ( ON ( DELETE | UPDATE ) ( SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION ) | MATCH identifier )* ]
  //     [ [ NOT ] DEFERRABLE [ INITIALLY DEFERRED | INITIALLY IMMEDIATE ] ]
  public static boolean foreign_key_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause")) return false;
    if (!nextTokenIs(b, REFERENCES_WORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REFERENCES_WORD);
    r = r && foreign_table(b, l + 1);
    r = r && foreign_key_clause_2(b, l + 1);
    r = r && foreign_key_clause_3(b, l + 1);
    r = r && foreign_key_clause_4(b, l + 1);
    exit_section_(b, m, FOREIGN_KEY_CLAUSE, r);
    return r;
  }

  // [ '(' column_name ( ',' column_name ) * ')' ]
  private static boolean foreign_key_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_2")) return false;
    foreign_key_clause_2_0(b, l + 1);
    return true;
  }

  // '(' column_name ( ',' column_name ) * ')'
  private static boolean foreign_key_clause_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && column_name(b, l + 1);
    r = r && foreign_key_clause_2_0_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_name ) *
  private static boolean foreign_key_clause_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_2_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreign_key_clause_2_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreign_key_clause_2_0_2", c)) break;
    }
    return true;
  }

  // ',' column_name
  private static boolean foreign_key_clause_2_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_2_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ( ON ( DELETE | UPDATE ) ( SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION ) | MATCH identifier )* ]
  private static boolean foreign_key_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3")) return false;
    foreign_key_clause_3_0(b, l + 1);
    return true;
  }

  // ( ON ( DELETE | UPDATE ) ( SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION ) | MATCH identifier )*
  private static boolean foreign_key_clause_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!foreign_key_clause_3_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "foreign_key_clause_3_0", c)) break;
    }
    return true;
  }

  // ON ( DELETE | UPDATE ) ( SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION ) | MATCH identifier
  private static boolean foreign_key_clause_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = foreign_key_clause_3_0_0_0(b, l + 1);
    if (!r) r = foreign_key_clause_3_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ON ( DELETE | UPDATE ) ( SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION )
  private static boolean foreign_key_clause_3_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ON);
    r = r && foreign_key_clause_3_0_0_0_1(b, l + 1);
    r = r && foreign_key_clause_3_0_0_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DELETE | UPDATE
  private static boolean foreign_key_clause_3_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0_0_0_1")) return false;
    boolean r;
    r = consumeToken(b, DELETE);
    if (!r) r = consumeToken(b, UPDATE);
    return r;
  }

  // SET NULL | SET DEFAULT | CASCADE | RESTRICT | NO ACTION
  private static boolean foreign_key_clause_3_0_0_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0_0_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, SET, NULL);
    if (!r) r = parseTokens(b, 0, SET, DEFAULT);
    if (!r) r = consumeToken(b, CASCADE);
    if (!r) r = consumeToken(b, RESTRICT);
    if (!r) r = parseTokens(b, 0, NO, ACTION);
    exit_section_(b, m, null, r);
    return r;
  }

  // MATCH identifier
  private static boolean foreign_key_clause_3_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_3_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MATCH);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ NOT ] DEFERRABLE [ INITIALLY DEFERRED | INITIALLY IMMEDIATE ] ]
  private static boolean foreign_key_clause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_4")) return false;
    foreign_key_clause_4_0(b, l + 1);
    return true;
  }

  // [ NOT ] DEFERRABLE [ INITIALLY DEFERRED | INITIALLY IMMEDIATE ]
  private static boolean foreign_key_clause_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = foreign_key_clause_4_0_0(b, l + 1);
    r = r && consumeToken(b, DEFERRABLE);
    r = r && foreign_key_clause_4_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean foreign_key_clause_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_4_0_0")) return false;
    consumeToken(b, NOT);
    return true;
  }

  // [ INITIALLY DEFERRED | INITIALLY IMMEDIATE ]
  private static boolean foreign_key_clause_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_4_0_2")) return false;
    foreign_key_clause_4_0_2_0(b, l + 1);
    return true;
  }

  // INITIALLY DEFERRED | INITIALLY IMMEDIATE
  private static boolean foreign_key_clause_4_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_key_clause_4_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, INITIALLY, DEFERRED);
    if (!r) r = parseTokens(b, 0, INITIALLY, IMMEDIATE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean foreign_table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreign_table")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, FOREIGN_TABLE, r);
    return r;
  }

  /* ********************************************************** */
  // id | IF
  public static boolean function_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_name")) return false;
    if (!nextTokenIs(b, "<function name>", ID, IF)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_NAME, "<function name>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, IF);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr ( ',' expr ) * [ HAVING expr ]
  public static boolean grouping_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUPING_TERM, "<grouping term>");
    r = expr(b, l + 1, -1);
    r = r && grouping_term_1(b, l + 1);
    r = r && grouping_term_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' expr ) *
  private static boolean grouping_term_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_term_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!grouping_term_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "grouping_term_1", c)) break;
    }
    return true;
  }

  // ',' expr
  private static boolean grouping_term_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_term_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ HAVING expr ]
  private static boolean grouping_term_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_term_2")) return false;
    grouping_term_2_0(b, l + 1);
    return true;
  }

  // HAVING expr
  private static boolean grouping_term_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grouping_term_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, HAVING);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean index_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, INDEX_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // column_name [ COLLATE collation_name ] [ ASC | DESC ]
  public static boolean indexed_column(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexed_column")) return false;
    if (!nextTokenIs(b, "<indexed column>", ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INDEXED_COLUMN, "<indexed column>");
    r = column_name(b, l + 1);
    r = r && indexed_column_1(b, l + 1);
    r = r && indexed_column_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ COLLATE collation_name ]
  private static boolean indexed_column_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexed_column_1")) return false;
    indexed_column_1_0(b, l + 1);
    return true;
  }

  // COLLATE collation_name
  private static boolean indexed_column_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexed_column_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLLATE);
    r = r && collation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ASC | DESC ]
  private static boolean indexed_column_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexed_column_2")) return false;
    indexed_column_2_0(b, l + 1);
    return true;
  }

  // ASC | DESC
  private static boolean indexed_column_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexed_column_2_0")) return false;
    boolean r;
    r = consumeToken(b, ASC);
    if (!r) r = consumeToken(b, DESC);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] ( INSERT OR REPLACE | REPLACE | INSERT OR ROLLBACK | INSERT OR ABORT | INSERT OR FAIL |
  //     INSERT OR IGNORE | INSERT ) INTO [ database_name '.' ] table_name [ '(' column_name ( ',' column_name ) * ')' ]
  //     ( VALUES values_expression ( ',' values_expression ) * | compound_select_stmt | DEFAULT VALUES )
  public static boolean insert_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INSERT_STMT, "<insert stmt>");
    r = insert_stmt_0(b, l + 1);
    r = r && insert_stmt_1(b, l + 1);
    r = r && consumeToken(b, INTO);
    r = r && insert_stmt_3(b, l + 1);
    r = r && table_name(b, l + 1);
    p = r; // pin = 5
    r = r && report_error_(b, insert_stmt_5(b, l + 1));
    r = p && insert_stmt_6(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ with_clause ]
  private static boolean insert_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // INSERT OR REPLACE | REPLACE | INSERT OR ROLLBACK | INSERT OR ABORT | INSERT OR FAIL |
  //     INSERT OR IGNORE | INSERT
  private static boolean insert_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, INSERT, OR, REPLACE);
    if (!r) r = consumeToken(b, REPLACE);
    if (!r) r = parseTokens(b, 0, INSERT, OR, ROLLBACK);
    if (!r) r = parseTokens(b, 0, INSERT, OR, ABORT);
    if (!r) r = parseTokens(b, 0, INSERT, OR, FAIL);
    if (!r) r = parseTokens(b, 0, INSERT, OR, IGNORE);
    if (!r) r = consumeToken(b, INSERT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ]
  private static boolean insert_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_3")) return false;
    insert_stmt_3_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean insert_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '(' column_name ( ',' column_name ) * ')' ]
  private static boolean insert_stmt_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_5")) return false;
    insert_stmt_5_0(b, l + 1);
    return true;
  }

  // '(' column_name ( ',' column_name ) * ')'
  private static boolean insert_stmt_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && column_name(b, l + 1);
    r = r && insert_stmt_5_0_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_name ) *
  private static boolean insert_stmt_5_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_5_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!insert_stmt_5_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "insert_stmt_5_0_2", c)) break;
    }
    return true;
  }

  // ',' column_name
  private static boolean insert_stmt_5_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_5_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VALUES values_expression ( ',' values_expression ) * | compound_select_stmt | DEFAULT VALUES
  private static boolean insert_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = insert_stmt_6_0(b, l + 1);
    if (!r) r = compound_select_stmt(b, l + 1);
    if (!r) r = parseTokens(b, 0, DEFAULT, VALUES);
    exit_section_(b, m, null, r);
    return r;
  }

  // VALUES values_expression ( ',' values_expression ) *
  private static boolean insert_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUES);
    r = r && values_expression(b, l + 1);
    r = r && insert_stmt_6_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' values_expression ) *
  private static boolean insert_stmt_6_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_6_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!insert_stmt_6_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "insert_stmt_6_0_2", c)) break;
    }
    return true;
  }

  // ',' values_expression
  private static boolean insert_stmt_6_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "insert_stmt_6_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && values_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // table_or_subquery ( join_operator table_or_subquery join_constraint )*
  public static boolean join_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_clause")) return false;
    if (!nextTokenIs(b, "<join clause>", ID, LP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JOIN_CLAUSE, "<join clause>");
    r = table_or_subquery(b, l + 1);
    r = r && join_clause_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( join_operator table_or_subquery join_constraint )*
  private static boolean join_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_clause_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!join_clause_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "join_clause_1", c)) break;
    }
    return true;
  }

  // join_operator table_or_subquery join_constraint
  private static boolean join_clause_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_clause_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = join_operator(b, l + 1);
    r = r && table_or_subquery(b, l + 1);
    r = r && join_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ ON expr | USING '(' column_name ( ',' column_name ) * ')' ]
  public static boolean join_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint")) return false;
    Marker m = enter_section_(b, l, _NONE_, JOIN_CONSTRAINT, "<join constraint>");
    join_constraint_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // ON expr | USING '(' column_name ( ',' column_name ) * ')'
  private static boolean join_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = join_constraint_0_0(b, l + 1);
    if (!r) r = join_constraint_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ON expr
  private static boolean join_constraint_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ON);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // USING '(' column_name ( ',' column_name ) * ')'
  private static boolean join_constraint_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, USING, LP);
    r = r && column_name(b, l + 1);
    r = r && join_constraint_0_1_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_name ) *
  private static boolean join_constraint_0_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint_0_1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!join_constraint_0_1_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "join_constraint_0_1_3", c)) break;
    }
    return true;
  }

  // ',' column_name
  private static boolean join_constraint_0_1_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_constraint_0_1_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ',' | [ NATURAL ] [ LEFT [ OUTER ] | INNER | CROSS ] JOIN
  public static boolean join_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JOIN_OPERATOR, "<join operator>");
    r = consumeToken(b, COMMA);
    if (!r) r = join_operator_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ NATURAL ] [ LEFT [ OUTER ] | INNER | CROSS ] JOIN
  private static boolean join_operator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = join_operator_1_0(b, l + 1);
    r = r && join_operator_1_1(b, l + 1);
    r = r && consumeToken(b, JOIN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NATURAL ]
  private static boolean join_operator_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1_0")) return false;
    consumeToken(b, NATURAL);
    return true;
  }

  // [ LEFT [ OUTER ] | INNER | CROSS ]
  private static boolean join_operator_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1_1")) return false;
    join_operator_1_1_0(b, l + 1);
    return true;
  }

  // LEFT [ OUTER ] | INNER | CROSS
  private static boolean join_operator_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = join_operator_1_1_0_0(b, l + 1);
    if (!r) r = consumeToken(b, INNER);
    if (!r) r = consumeToken(b, CROSS);
    exit_section_(b, m, null, r);
    return r;
  }

  // LEFT [ OUTER ]
  private static boolean join_operator_1_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LEFT);
    r = r && join_operator_1_1_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ OUTER ]
  private static boolean join_operator_1_1_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "join_operator_1_1_0_0_1")) return false;
    consumeToken(b, OUTER);
    return true;
  }

  /* ********************************************************** */
  // expr
  public static boolean limiting_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "limiting_term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LIMITING_TERM, "<limiting term>");
    r = expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // numeric_literal
  //                   | string_literal
  //                   | blob_literal
  //                   | NULL
  //                   | CURRENT_TIME
  //                   | CURRENT_DATE
  //                   | CURRENT_TIMESTAMP
  public static boolean literal_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_VALUE, "<literal value>");
    r = numeric_literal(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    if (!r) r = blob_literal(b, l + 1);
    if (!r) r = consumeToken(b, NULL);
    if (!r) r = consumeToken(b, CURRENT_TIME);
    if (!r) r = consumeToken(b, CURRENT_DATE);
    if (!r) r = consumeToken(b, CURRENT_TIMESTAMP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ column_def ]
  public static boolean module_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_argument")) return false;
    Marker m = enter_section_(b, l, _NONE_, MODULE_ARGUMENT, "<module argument>");
    column_def(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // id
  public static boolean module_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, MODULE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean new_table_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "new_table_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, NEW_TABLE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // ( ( digit [ '.' ( digit ) * ] | '.' digit ) | TRUE | FALSE ) [ E [ '+' | '-' ] digit ]
  public static boolean numeric_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMERIC_LITERAL, "<numeric literal>");
    r = numeric_literal_0(b, l + 1);
    r = r && numeric_literal_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( digit [ '.' ( digit ) * ] | '.' digit ) | TRUE | FALSE
  private static boolean numeric_literal_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = numeric_literal_0_0(b, l + 1);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // digit [ '.' ( digit ) * ] | '.' digit
  private static boolean numeric_literal_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = numeric_literal_0_0_0(b, l + 1);
    if (!r) r = parseTokens(b, 0, DOT, DIGIT);
    exit_section_(b, m, null, r);
    return r;
  }

  // digit [ '.' ( digit ) * ]
  private static boolean numeric_literal_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DIGIT);
    r = r && numeric_literal_0_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '.' ( digit ) * ]
  private static boolean numeric_literal_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0_0_0_1")) return false;
    numeric_literal_0_0_0_1_0(b, l + 1);
    return true;
  }

  // '.' ( digit ) *
  private static boolean numeric_literal_0_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && numeric_literal_0_0_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( digit ) *
  private static boolean numeric_literal_0_0_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_0_0_0_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, DIGIT)) break;
      if (!empty_element_parsed_guard_(b, "numeric_literal_0_0_0_1_0_1", c)) break;
    }
    return true;
  }

  // [ E [ '+' | '-' ] digit ]
  private static boolean numeric_literal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_1")) return false;
    numeric_literal_1_0(b, l + 1);
    return true;
  }

  // E [ '+' | '-' ] digit
  private static boolean numeric_literal_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, E);
    r = r && numeric_literal_1_0_1(b, l + 1);
    r = r && consumeToken(b, DIGIT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '+' | '-' ]
  private static boolean numeric_literal_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_1_0_1")) return false;
    numeric_literal_1_0_1_0(b, l + 1);
    return true;
  }

  // '+' | '-'
  private static boolean numeric_literal_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "numeric_literal_1_0_1_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // expr [ COLLATE collation_name ] [ ASC | DESC ]
  public static boolean ordering_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ORDERING_TERM, "<ordering term>");
    r = expr(b, l + 1, -1);
    r = r && ordering_term_1(b, l + 1);
    r = r && ordering_term_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ COLLATE collation_name ]
  private static boolean ordering_term_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_term_1")) return false;
    ordering_term_1_0(b, l + 1);
    return true;
  }

  // COLLATE collation_name
  private static boolean ordering_term_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_term_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLLATE);
    r = r && collation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ASC | DESC ]
  private static boolean ordering_term_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_term_2")) return false;
    ordering_term_2_0(b, l + 1);
    return true;
  }

  // ASC | DESC
  private static boolean ordering_term_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordering_term_2_0")) return false;
    boolean r;
    r = consumeToken(b, ASC);
    if (!r) r = consumeToken(b, DESC);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean pragma_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, PRAGMA_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // PRAGMA [ database_name '.' ] pragma_name [ '=' pragma_value | '(' pragma_value ')' ]
  public static boolean pragma_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt")) return false;
    if (!nextTokenIs(b, PRAGMA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PRAGMA_STMT, null);
    r = consumeToken(b, PRAGMA);
    p = r; // pin = 1
    r = r && report_error_(b, pragma_stmt_1(b, l + 1));
    r = p && report_error_(b, pragma_name(b, l + 1)) && r;
    r = p && pragma_stmt_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ database_name '.' ]
  private static boolean pragma_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_1")) return false;
    pragma_stmt_1_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean pragma_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '=' pragma_value | '(' pragma_value ')' ]
  private static boolean pragma_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_3")) return false;
    pragma_stmt_3_0(b, l + 1);
    return true;
  }

  // '=' pragma_value | '(' pragma_value ')'
  private static boolean pragma_stmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pragma_stmt_3_0_0(b, l + 1);
    if (!r) r = pragma_stmt_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' pragma_value
  private static boolean pragma_stmt_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && pragma_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' pragma_value ')'
  private static boolean pragma_stmt_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_stmt_3_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && pragma_value(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // signed_number | identifier | string_literal
  public static boolean pragma_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pragma_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRAGMA_VALUE, "<pragma value>");
    r = signed_number(b, l + 1);
    if (!r) r = identifier(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ database_name '.' ] table_name [ INDEXED BY index_name | NOT INDEXED ]
  public static boolean qualified_table_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qualified_table_name_0(b, l + 1);
    r = r && table_name(b, l + 1);
    r = r && qualified_table_name_2(b, l + 1);
    exit_section_(b, m, QUALIFIED_TABLE_NAME, r);
    return r;
  }

  // [ database_name '.' ]
  private static boolean qualified_table_name_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name_0")) return false;
    qualified_table_name_0_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean qualified_table_name_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ INDEXED BY index_name | NOT INDEXED ]
  private static boolean qualified_table_name_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name_2")) return false;
    qualified_table_name_2_0(b, l + 1);
    return true;
  }

  // INDEXED BY index_name | NOT INDEXED
  private static boolean qualified_table_name_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qualified_table_name_2_0_0(b, l + 1);
    if (!r) r = parseTokens(b, 0, NOT, INDEXED);
    exit_section_(b, m, null, r);
    return r;
  }

  // INDEXED BY index_name
  private static boolean qualified_table_name_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qualified_table_name_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, INDEXED, BY);
    r = r && index_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // RAISE '(' ( IGNORE | ( ROLLBACK | ABORT | FAIL ) ',' error_message ) ')'
  public static boolean raise_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "raise_function")) return false;
    if (!nextTokenIs(b, RAISE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RAISE, LP);
    r = r && raise_function_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, RAISE_FUNCTION, r);
    return r;
  }

  // IGNORE | ( ROLLBACK | ABORT | FAIL ) ',' error_message
  private static boolean raise_function_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "raise_function_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IGNORE);
    if (!r) r = raise_function_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ROLLBACK | ABORT | FAIL ) ',' error_message
  private static boolean raise_function_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "raise_function_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = raise_function_2_1_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && error_message(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ROLLBACK | ABORT | FAIL
  private static boolean raise_function_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "raise_function_2_1_0")) return false;
    boolean r;
    r = consumeToken(b, ROLLBACK);
    if (!r) r = consumeToken(b, ABORT);
    if (!r) r = consumeToken(b, FAIL);
    return r;
  }

  /* ********************************************************** */
  // REINDEX [ collation_name | [ database_name '.' ] ( table_name | index_name ) ]
  public static boolean reindex_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt")) return false;
    if (!nextTokenIs(b, REINDEX)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REINDEX_STMT, null);
    r = consumeToken(b, REINDEX);
    p = r; // pin = 1
    r = r && reindex_stmt_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ collation_name | [ database_name '.' ] ( table_name | index_name ) ]
  private static boolean reindex_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1")) return false;
    reindex_stmt_1_0(b, l + 1);
    return true;
  }

  // collation_name | [ database_name '.' ] ( table_name | index_name )
  private static boolean reindex_stmt_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = collation_name(b, l + 1);
    if (!r) r = reindex_stmt_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ] ( table_name | index_name )
  private static boolean reindex_stmt_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reindex_stmt_1_0_1_0(b, l + 1);
    r = r && reindex_stmt_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ]
  private static boolean reindex_stmt_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1_0_1_0")) return false;
    reindex_stmt_1_0_1_0_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean reindex_stmt_1_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1_0_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_name | index_name
  private static boolean reindex_stmt_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reindex_stmt_1_0_1_1")) return false;
    boolean r;
    r = table_name(b, l + 1);
    if (!r) r = index_name(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // RELEASE [ SAVEPOINT ] savepoint_name
  public static boolean release_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "release_stmt")) return false;
    if (!nextTokenIs(b, RELEASE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RELEASE_STMT, null);
    r = consumeToken(b, RELEASE);
    p = r; // pin = 1
    r = r && report_error_(b, release_stmt_1(b, l + 1));
    r = p && savepoint_name(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ SAVEPOINT ]
  private static boolean release_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "release_stmt_1")) return false;
    consumeToken(b, SAVEPOINT);
    return true;
  }

  /* ********************************************************** */
  // '*' | table_name '.' '*' | expr [ [ AS ] column_alias ]
  public static boolean result_column(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESULT_COLUMN, "<result column>");
    r = consumeToken(b, MULTIPLY);
    if (!r) r = result_column_1(b, l + 1);
    if (!r) r = result_column_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // table_name '.' '*'
  private static boolean result_column_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_name(b, l + 1);
    r = r && consumeTokens(b, 0, DOT, MULTIPLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr [ [ AS ] column_alias ]
  private static boolean result_column_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1, -1);
    r = r && result_column_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ AS ] column_alias ]
  private static boolean result_column_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column_2_1")) return false;
    result_column_2_1_0(b, l + 1);
    return true;
  }

  // [ AS ] column_alias
  private static boolean result_column_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = result_column_2_1_0_0(b, l + 1);
    r = r && column_alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean result_column_2_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "result_column_2_1_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  /* ********************************************************** */
  // ROLLBACK [ TRANSACTION ] [ TO [ SAVEPOINT ] savepoint_name ]
  public static boolean rollback_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rollback_stmt")) return false;
    if (!nextTokenIs(b, ROLLBACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ROLLBACK_STMT, null);
    r = consumeToken(b, ROLLBACK);
    p = r; // pin = 1
    r = r && report_error_(b, rollback_stmt_1(b, l + 1));
    r = p && rollback_stmt_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ TRANSACTION ]
  private static boolean rollback_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rollback_stmt_1")) return false;
    consumeToken(b, TRANSACTION);
    return true;
  }

  // [ TO [ SAVEPOINT ] savepoint_name ]
  private static boolean rollback_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rollback_stmt_2")) return false;
    rollback_stmt_2_0(b, l + 1);
    return true;
  }

  // TO [ SAVEPOINT ] savepoint_name
  private static boolean rollback_stmt_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rollback_stmt_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TO);
    r = r && rollback_stmt_2_0_1(b, l + 1);
    r = r && savepoint_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ SAVEPOINT ]
  private static boolean rollback_stmt_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rollback_stmt_2_0_1")) return false;
    consumeToken(b, SAVEPOINT);
    return true;
  }

  /* ********************************************************** */
  // statement SEMI?
  public static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ROOT, "<root>");
    r = statement(b, l + 1);
    p = r; // pin = 1
    r = r && root_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // SEMI?
  private static boolean root_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root_1")) return false;
    consumeToken(b, SEMI);
    return true;
  }

  /* ********************************************************** */
  // id
  public static boolean savepoint_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "savepoint_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, SAVEPOINT_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // SAVEPOINT savepoint_name
  public static boolean savepoint_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "savepoint_stmt")) return false;
    if (!nextTokenIs(b, SAVEPOINT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SAVEPOINT_STMT, null);
    r = consumeToken(b, SAVEPOINT);
    p = r; // pin = 1
    r = r && savepoint_name(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // SELECT [ DISTINCT | ALL ] compound_result_column [ FROM join_clause ] [ [ WHERE ] expr ] [ GROUP BY grouping_term ]
  //     [ ORDER BY ordering_term ( ',' ordering_term ) * ] | VALUES values_expression ( ',' values_expression ) *
  public static boolean select_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt")) return false;
    if (!nextTokenIs(b, "<select stmt>", SELECT, VALUES)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SELECT_STMT, "<select stmt>");
    r = select_stmt_0(b, l + 1);
    if (!r) r = select_stmt_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SELECT [ DISTINCT | ALL ] compound_result_column [ FROM join_clause ] [ [ WHERE ] expr ] [ GROUP BY grouping_term ]
  //     [ ORDER BY ordering_term ( ',' ordering_term ) * ]
  private static boolean select_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SELECT);
    r = r && select_stmt_0_1(b, l + 1);
    r = r && compound_result_column(b, l + 1);
    r = r && select_stmt_0_3(b, l + 1);
    r = r && select_stmt_0_4(b, l + 1);
    r = r && select_stmt_0_5(b, l + 1);
    r = r && select_stmt_0_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ DISTINCT | ALL ]
  private static boolean select_stmt_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_1")) return false;
    select_stmt_0_1_0(b, l + 1);
    return true;
  }

  // DISTINCT | ALL
  private static boolean select_stmt_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_1_0")) return false;
    boolean r;
    r = consumeToken(b, DISTINCT);
    if (!r) r = consumeToken(b, ALL);
    return r;
  }

  // [ FROM join_clause ]
  private static boolean select_stmt_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_3")) return false;
    select_stmt_0_3_0(b, l + 1);
    return true;
  }

  // FROM join_clause
  private static boolean select_stmt_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FROM);
    r = r && join_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ WHERE ] expr ]
  private static boolean select_stmt_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_4")) return false;
    select_stmt_0_4_0(b, l + 1);
    return true;
  }

  // [ WHERE ] expr
  private static boolean select_stmt_0_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = select_stmt_0_4_0_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ WHERE ]
  private static boolean select_stmt_0_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_4_0_0")) return false;
    consumeToken(b, WHERE);
    return true;
  }

  // [ GROUP BY grouping_term ]
  private static boolean select_stmt_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_5")) return false;
    select_stmt_0_5_0(b, l + 1);
    return true;
  }

  // GROUP BY grouping_term
  private static boolean select_stmt_0_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, GROUP, BY);
    r = r && grouping_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ]
  private static boolean select_stmt_0_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_6")) return false;
    select_stmt_0_6_0(b, l + 1);
    return true;
  }

  // ORDER BY ordering_term ( ',' ordering_term ) *
  private static boolean select_stmt_0_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ORDER, BY);
    r = r && ordering_term(b, l + 1);
    r = r && select_stmt_0_6_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' ordering_term ) *
  private static boolean select_stmt_0_6_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_6_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!select_stmt_0_6_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "select_stmt_0_6_0_3", c)) break;
    }
    return true;
  }

  // ',' ordering_term
  private static boolean select_stmt_0_6_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_0_6_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ordering_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // VALUES values_expression ( ',' values_expression ) *
  private static boolean select_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUES);
    r = r && values_expression(b, l + 1);
    r = r && select_stmt_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' values_expression ) *
  private static boolean select_stmt_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!select_stmt_1_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "select_stmt_1_2", c)) break;
    }
    return true;
  }

  // ',' values_expression
  private static boolean select_stmt_1_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select_stmt_1_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && values_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr
  public static boolean setter_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "setter_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SETTER_EXPRESSION, "<setter expression>");
    r = expr(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [ '+' | '-' ] numeric_literal
  public static boolean signed_number(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signed_number")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIGNED_NUMBER, "<signed number>");
    r = signed_number_0(b, l + 1);
    r = r && numeric_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ '+' | '-' ]
  private static boolean signed_number_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signed_number_0")) return false;
    signed_number_0_0(b, l + 1);
    return true;
  }

  // '+' | '-'
  private static boolean signed_number_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signed_number_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // root*
  static boolean sqlFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sqlFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!root(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sqlFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !( ';' | ALTER | ANALYZE | ATTACH | BEGIN | COMMIT | END | ROLLBACK | SAVEPOINT | RELEASE |
  //     CREATE | DROP | INSERT | WITH | UPDATE | DELETE | SELECT | PRAGMA | REINDEX)
  static boolean sql_stmt_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sql_stmt_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !sql_stmt_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ';' | ALTER | ANALYZE | ATTACH | BEGIN | COMMIT | END | ROLLBACK | SAVEPOINT | RELEASE |
  //     CREATE | DROP | INSERT | WITH | UPDATE | DELETE | SELECT | PRAGMA | REINDEX
  private static boolean sql_stmt_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sql_stmt_recovery_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, ALTER);
    if (!r) r = consumeToken(b, ANALYZE);
    if (!r) r = consumeToken(b, ATTACH);
    if (!r) r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, COMMIT);
    if (!r) r = consumeToken(b, END);
    if (!r) r = consumeToken(b, ROLLBACK);
    if (!r) r = consumeToken(b, SAVEPOINT);
    if (!r) r = consumeToken(b, RELEASE);
    if (!r) r = consumeToken(b, CREATE);
    if (!r) r = consumeToken(b, DROP);
    if (!r) r = consumeToken(b, INSERT);
    if (!r) r = consumeToken(b, WITH);
    if (!r) r = consumeToken(b, UPDATE);
    if (!r) r = consumeToken(b, DELETE);
    if (!r) r = consumeToken(b, SELECT);
    if (!r) r = consumeToken(b, PRAGMA);
    if (!r) r = consumeToken(b, REINDEX);
    return r;
  }

  /* ********************************************************** */
  // [ EXPLAIN | DESC ] (
  //     alter_table_stmt | analyze_stmt | attach_stmt | begin_stmt | commit_stmt | create_index_stmt | create_table_stmt |
  //     create_trigger_stmt | create_view_stmt | create_virtual_table_stmt | delete_stmt_limited | detach_stmt | drop_index_stmt |
  //     drop_table_stmt | drop_trigger_stmt | drop_view_stmt | insert_stmt | pragma_stmt | reindex_stmt | release_stmt |
  //     rollback_stmt | savepoint_stmt | compound_select_stmt | update_stmt_limited | vacuum_stmt
  // )
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = statement_0(b, l + 1);
    r = r && statement_1(b, l + 1);
    exit_section_(b, l, m, r, false, SqlParser::sql_stmt_recovery);
    return r;
  }

  // [ EXPLAIN | DESC ]
  private static boolean statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_0")) return false;
    statement_0_0(b, l + 1);
    return true;
  }

  // EXPLAIN | DESC
  private static boolean statement_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_0_0")) return false;
    boolean r;
    r = consumeToken(b, EXPLAIN);
    if (!r) r = consumeToken(b, DESC);
    return r;
  }

  // alter_table_stmt | analyze_stmt | attach_stmt | begin_stmt | commit_stmt | create_index_stmt | create_table_stmt |
  //     create_trigger_stmt | create_view_stmt | create_virtual_table_stmt | delete_stmt_limited | detach_stmt | drop_index_stmt |
  //     drop_table_stmt | drop_trigger_stmt | drop_view_stmt | insert_stmt | pragma_stmt | reindex_stmt | release_stmt |
  //     rollback_stmt | savepoint_stmt | compound_select_stmt | update_stmt_limited | vacuum_stmt
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    boolean r;
    r = alter_table_stmt(b, l + 1);
    if (!r) r = analyze_stmt(b, l + 1);
    if (!r) r = attach_stmt(b, l + 1);
    if (!r) r = begin_stmt(b, l + 1);
    if (!r) r = commit_stmt(b, l + 1);
    if (!r) r = create_index_stmt(b, l + 1);
    if (!r) r = create_table_stmt(b, l + 1);
    if (!r) r = create_trigger_stmt(b, l + 1);
    if (!r) r = create_view_stmt(b, l + 1);
    if (!r) r = create_virtual_table_stmt(b, l + 1);
    if (!r) r = delete_stmt_limited(b, l + 1);
    if (!r) r = detach_stmt(b, l + 1);
    if (!r) r = drop_index_stmt(b, l + 1);
    if (!r) r = drop_table_stmt(b, l + 1);
    if (!r) r = drop_trigger_stmt(b, l + 1);
    if (!r) r = drop_view_stmt(b, l + 1);
    if (!r) r = insert_stmt(b, l + 1);
    if (!r) r = pragma_stmt(b, l + 1);
    if (!r) r = reindex_stmt(b, l + 1);
    if (!r) r = release_stmt(b, l + 1);
    if (!r) r = rollback_stmt(b, l + 1);
    if (!r) r = savepoint_stmt(b, l + 1);
    if (!r) r = compound_select_stmt(b, l + 1);
    if (!r) r = update_stmt_limited(b, l + 1);
    if (!r) r = vacuum_stmt(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // string
  public static boolean string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, STRING_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean table_alias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_alias")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, TABLE_ALIAS, r);
    return r;
  }

  /* ********************************************************** */
  // attr_name [ '=' attr_value ]
  public static boolean table_attr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_attr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_ATTR, "<table attr>");
    r = attr_name(b, l + 1);
    r = r && table_attr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ '=' attr_value ]
  private static boolean table_attr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_attr_1")) return false;
    table_attr_1_0(b, l + 1);
    return true;
  }

  // '=' attr_value
  private static boolean table_attr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_attr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && attr_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ CONSTRAINT identifier ] (
  //     ( PRIMARY KEY | UNIQUE KEY | UNIQUE | KEY ) [ index_name ] '(' indexed_column ( ',' indexed_column ) * ')' conflict_clause | CHECK '(' expr ')'
  //     | FOREIGN KEY '(' column_name ( ',' column_name ) * ')' foreign_key_clause )
  public static boolean table_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_CONSTRAINT, "<table constraint>");
    r = table_constraint_0(b, l + 1);
    r = r && table_constraint_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ CONSTRAINT identifier ]
  private static boolean table_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_0")) return false;
    table_constraint_0_0(b, l + 1);
    return true;
  }

  // CONSTRAINT identifier
  private static boolean table_constraint_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONSTRAINT);
    r = r && identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( PRIMARY KEY | UNIQUE KEY | UNIQUE | KEY ) [ index_name ] '(' indexed_column ( ',' indexed_column ) * ')' conflict_clause | CHECK '(' expr ')'
  //     | FOREIGN KEY '(' column_name ( ',' column_name ) * ')' foreign_key_clause
  private static boolean table_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_constraint_1_0(b, l + 1);
    if (!r) r = table_constraint_1_1(b, l + 1);
    if (!r) r = table_constraint_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( PRIMARY KEY | UNIQUE KEY | UNIQUE | KEY ) [ index_name ] '(' indexed_column ( ',' indexed_column ) * ')' conflict_clause
  private static boolean table_constraint_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_constraint_1_0_0(b, l + 1);
    r = r && table_constraint_1_0_1(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && indexed_column(b, l + 1);
    r = r && table_constraint_1_0_4(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && conflict_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PRIMARY KEY | UNIQUE KEY | UNIQUE | KEY
  private static boolean table_constraint_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, PRIMARY, KEY);
    if (!r) r = parseTokens(b, 0, UNIQUE, KEY);
    if (!r) r = consumeToken(b, UNIQUE);
    if (!r) r = consumeToken(b, KEY);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ index_name ]
  private static boolean table_constraint_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_0_1")) return false;
    index_name(b, l + 1);
    return true;
  }

  // ( ',' indexed_column ) *
  private static boolean table_constraint_1_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_0_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_constraint_1_0_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_constraint_1_0_4", c)) break;
    }
    return true;
  }

  // ',' indexed_column
  private static boolean table_constraint_1_0_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_0_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && indexed_column(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // CHECK '(' expr ')'
  private static boolean table_constraint_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CHECK, LP);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // FOREIGN KEY '(' column_name ( ',' column_name ) * ')' foreign_key_clause
  private static boolean table_constraint_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, FOREIGN, KEY, LP);
    r = r && column_name(b, l + 1);
    r = r && table_constraint_1_2_4(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && foreign_key_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' column_name ) *
  private static boolean table_constraint_1_2_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_2_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_constraint_1_2_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_constraint_1_2_4", c)) break;
    }
    return true;
  }

  // ',' column_name
  private static boolean table_constraint_1_2_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_constraint_1_2_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && column_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean table_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, TABLE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean table_or_index_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_index_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, TABLE_OR_INDEX_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [ database_name '.' ] table_name [ [ AS ] table_alias ] [ INDEXED BY index_name | NOT INDEXED ]
  //                       | '(' ( table_or_subquery ( ',' table_or_subquery ) * | join_clause ) ')'
  //                       | '(' compound_select_stmt ')' [ [ AS ] table_alias ]
  public static boolean table_or_subquery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery")) return false;
    if (!nextTokenIs(b, "<table or subquery>", ID, LP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TABLE_OR_SUBQUERY, "<table or subquery>");
    r = table_or_subquery_0(b, l + 1);
    if (!r) r = table_or_subquery_1(b, l + 1);
    if (!r) r = table_or_subquery_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ database_name '.' ] table_name [ [ AS ] table_alias ] [ INDEXED BY index_name | NOT INDEXED ]
  private static boolean table_or_subquery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery_0_0(b, l + 1);
    r = r && table_name(b, l + 1);
    r = r && table_or_subquery_0_2(b, l + 1);
    r = r && table_or_subquery_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ]
  private static boolean table_or_subquery_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_0")) return false;
    table_or_subquery_0_0_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean table_or_subquery_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ AS ] table_alias ]
  private static boolean table_or_subquery_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_2")) return false;
    table_or_subquery_0_2_0(b, l + 1);
    return true;
  }

  // [ AS ] table_alias
  private static boolean table_or_subquery_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery_0_2_0_0(b, l + 1);
    r = r && table_alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean table_or_subquery_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_2_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  // [ INDEXED BY index_name | NOT INDEXED ]
  private static boolean table_or_subquery_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_3")) return false;
    table_or_subquery_0_3_0(b, l + 1);
    return true;
  }

  // INDEXED BY index_name | NOT INDEXED
  private static boolean table_or_subquery_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery_0_3_0_0(b, l + 1);
    if (!r) r = parseTokens(b, 0, NOT, INDEXED);
    exit_section_(b, m, null, r);
    return r;
  }

  // INDEXED BY index_name
  private static boolean table_or_subquery_0_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_0_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, INDEXED, BY);
    r = r && index_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' ( table_or_subquery ( ',' table_or_subquery ) * | join_clause ) ')'
  private static boolean table_or_subquery_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && table_or_subquery_1_1(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_or_subquery ( ',' table_or_subquery ) * | join_clause
  private static boolean table_or_subquery_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery_1_1_0(b, l + 1);
    if (!r) r = join_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_or_subquery ( ',' table_or_subquery ) *
  private static boolean table_or_subquery_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery(b, l + 1);
    r = r && table_or_subquery_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' table_or_subquery ) *
  private static boolean table_or_subquery_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_1_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_or_subquery_1_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_or_subquery_1_1_0_1", c)) break;
    }
    return true;
  }

  // ',' table_or_subquery
  private static boolean table_or_subquery_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && table_or_subquery(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' compound_select_stmt ')' [ [ AS ] table_alias ]
  private static boolean table_or_subquery_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && compound_select_stmt(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && table_or_subquery_2_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ AS ] table_alias ]
  private static boolean table_or_subquery_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_2_3")) return false;
    table_or_subquery_2_3_0(b, l + 1);
    return true;
  }

  // [ AS ] table_alias
  private static boolean table_or_subquery_2_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_2_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_or_subquery_2_3_0_0(b, l + 1);
    r = r && table_alias(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ AS ]
  private static boolean table_or_subquery_2_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_or_subquery_2_3_0_0")) return false;
    consumeToken(b, AS);
    return true;
  }

  /* ********************************************************** */
  // id
  public static boolean trigger_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trigger_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, TRIGGER_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // identifier [ '(' signed_number ')' | '(' signed_number ',' signed_number ')' ]
  public static boolean type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = identifier(b, l + 1);
    r = r && type_name_1(b, l + 1);
    exit_section_(b, m, TYPE_NAME, r);
    return r;
  }

  // [ '(' signed_number ')' | '(' signed_number ',' signed_number ')' ]
  private static boolean type_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1")) return false;
    type_name_1_0(b, l + 1);
    return true;
  }

  // '(' signed_number ')' | '(' signed_number ',' signed_number ')'
  private static boolean type_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_name_1_0_0(b, l + 1);
    if (!r) r = type_name_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' signed_number ')'
  private static boolean type_name_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && signed_number(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' signed_number ',' signed_number ')'
  private static boolean type_name_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && signed_number(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && signed_number(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] UPDATE [ OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE ] qualified_table_name
  //     SET column_name '=' setter_expression update_stmt_subsequent_setter * [ WHERE expr ]
  public static boolean update_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt")) return false;
    if (!nextTokenIs(b, "<update stmt>", UPDATE, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UPDATE_STMT, "<update stmt>");
    r = update_stmt_0(b, l + 1);
    r = r && consumeToken(b, UPDATE);
    r = r && update_stmt_2(b, l + 1);
    r = r && qualified_table_name(b, l + 1);
    p = r; // pin = 4
    r = r && report_error_(b, consumeToken(b, SET));
    r = p && report_error_(b, column_name(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, EQ)) && r;
    r = p && report_error_(b, setter_expression(b, l + 1)) && r;
    r = p && report_error_(b, update_stmt_8(b, l + 1)) && r;
    r = p && update_stmt_9(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ with_clause ]
  private static boolean update_stmt_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // [ OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE ]
  private static boolean update_stmt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_2")) return false;
    update_stmt_2_0(b, l + 1);
    return true;
  }

  // OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE
  private static boolean update_stmt_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, OR, ROLLBACK);
    if (!r) r = parseTokens(b, 0, OR, ABORT);
    if (!r) r = parseTokens(b, 0, OR, REPLACE);
    if (!r) r = parseTokens(b, 0, OR, FAIL);
    if (!r) r = parseTokens(b, 0, OR, IGNORE);
    exit_section_(b, m, null, r);
    return r;
  }

  // update_stmt_subsequent_setter *
  private static boolean update_stmt_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_8")) return false;
    while (true) {
      int c = current_position_(b);
      if (!update_stmt_subsequent_setter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "update_stmt_8", c)) break;
    }
    return true;
  }

  // [ WHERE expr ]
  private static boolean update_stmt_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_9")) return false;
    update_stmt_9_0(b, l + 1);
    return true;
  }

  // WHERE expr
  private static boolean update_stmt_9_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_9_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHERE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ with_clause ] UPDATE [ OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE ] qualified_table_name
  //     SET column_name '=' setter_expression update_stmt_subsequent_setter * [ WHERE expr ]
  //     [ [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ] ]
  public static boolean update_stmt_limited(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited")) return false;
    if (!nextTokenIs(b, "<update stmt limited>", UPDATE, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UPDATE_STMT_LIMITED, "<update stmt limited>");
    r = update_stmt_limited_0(b, l + 1);
    r = r && consumeToken(b, UPDATE);
    r = r && update_stmt_limited_2(b, l + 1);
    r = r && qualified_table_name(b, l + 1);
    p = r; // pin = 4
    r = r && report_error_(b, consumeToken(b, SET));
    r = p && report_error_(b, column_name(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, EQ)) && r;
    r = p && report_error_(b, setter_expression(b, l + 1)) && r;
    r = p && report_error_(b, update_stmt_limited_8(b, l + 1)) && r;
    r = p && report_error_(b, update_stmt_limited_9(b, l + 1)) && r;
    r = p && update_stmt_limited_10(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ with_clause ]
  private static boolean update_stmt_limited_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_0")) return false;
    with_clause(b, l + 1);
    return true;
  }

  // [ OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE ]
  private static boolean update_stmt_limited_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_2")) return false;
    update_stmt_limited_2_0(b, l + 1);
    return true;
  }

  // OR ROLLBACK | OR ABORT | OR REPLACE | OR FAIL | OR IGNORE
  private static boolean update_stmt_limited_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, OR, ROLLBACK);
    if (!r) r = parseTokens(b, 0, OR, ABORT);
    if (!r) r = parseTokens(b, 0, OR, REPLACE);
    if (!r) r = parseTokens(b, 0, OR, FAIL);
    if (!r) r = parseTokens(b, 0, OR, IGNORE);
    exit_section_(b, m, null, r);
    return r;
  }

  // update_stmt_subsequent_setter *
  private static boolean update_stmt_limited_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_8")) return false;
    while (true) {
      int c = current_position_(b);
      if (!update_stmt_subsequent_setter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "update_stmt_limited_8", c)) break;
    }
    return true;
  }

  // [ WHERE expr ]
  private static boolean update_stmt_limited_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_9")) return false;
    update_stmt_limited_9_0(b, l + 1);
    return true;
  }

  // WHERE expr
  private static boolean update_stmt_limited_9_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_9_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHERE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ] ]
  private static boolean update_stmt_limited_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10")) return false;
    update_stmt_limited_10_0(b, l + 1);
    return true;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ] LIMIT expr [ ( OFFSET | ',' ) expr ]
  private static boolean update_stmt_limited_10_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = update_stmt_limited_10_0_0(b, l + 1);
    r = r && consumeToken(b, LIMIT);
    r = r && expr(b, l + 1, -1);
    r = r && update_stmt_limited_10_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ORDER BY ordering_term ( ',' ordering_term ) * ]
  private static boolean update_stmt_limited_10_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_0")) return false;
    update_stmt_limited_10_0_0_0(b, l + 1);
    return true;
  }

  // ORDER BY ordering_term ( ',' ordering_term ) *
  private static boolean update_stmt_limited_10_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ORDER, BY);
    r = r && ordering_term(b, l + 1);
    r = r && update_stmt_limited_10_0_0_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' ordering_term ) *
  private static boolean update_stmt_limited_10_0_0_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_0_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!update_stmt_limited_10_0_0_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "update_stmt_limited_10_0_0_0_3", c)) break;
    }
    return true;
  }

  // ',' ordering_term
  private static boolean update_stmt_limited_10_0_0_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_0_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ordering_term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ( OFFSET | ',' ) expr ]
  private static boolean update_stmt_limited_10_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_3")) return false;
    update_stmt_limited_10_0_3_0(b, l + 1);
    return true;
  }

  // ( OFFSET | ',' ) expr
  private static boolean update_stmt_limited_10_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = update_stmt_limited_10_0_3_0_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OFFSET | ','
  private static boolean update_stmt_limited_10_0_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_limited_10_0_3_0_0")) return false;
    boolean r;
    r = consumeToken(b, OFFSET);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // ',' column_name '=' setter_expression
  public static boolean update_stmt_subsequent_setter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "update_stmt_subsequent_setter")) return false;
    if (!nextTokenIs(b, COMMA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UPDATE_STMT_SUBSEQUENT_SETTER, null);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && report_error_(b, column_name(b, l + 1));
    r = p && report_error_(b, consumeToken(b, EQ)) && r;
    r = p && setter_expression(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // VACUUM
  public static boolean vacuum_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vacuum_stmt")) return false;
    if (!nextTokenIs(b, VACUUM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VACUUM);
    exit_section_(b, m, VACUUM_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // '(' expr ( ',' expr ) * ')'
  public static boolean values_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "values_expression")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && expr(b, l + 1, -1);
    r = r && values_expression_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, VALUES_EXPRESSION, r);
    return r;
  }

  // ( ',' expr ) *
  private static boolean values_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "values_expression_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!values_expression_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "values_expression_2", c)) break;
    }
    return true;
  }

  // ',' expr
  private static boolean values_expression_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "values_expression_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean view_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "view_name")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, VIEW_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // WITH [ RECURSIVE ] cte_table_name AS '(' compound_select_stmt ')' ( ',' cte_table_name AS '(' compound_select_stmt ')' ) *
  public static boolean with_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_clause")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WITH_CLAUSE, null);
    r = consumeToken(b, WITH);
    p = r; // pin = 1
    r = r && report_error_(b, with_clause_1(b, l + 1));
    r = p && report_error_(b, cte_table_name(b, l + 1)) && r;
    r = p && report_error_(b, consumeTokens(b, -1, AS, LP)) && r;
    r = p && report_error_(b, compound_select_stmt(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RP)) && r;
    r = p && with_clause_7(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ RECURSIVE ]
  private static boolean with_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_clause_1")) return false;
    consumeToken(b, RECURSIVE);
    return true;
  }

  // ( ',' cte_table_name AS '(' compound_select_stmt ')' ) *
  private static boolean with_clause_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_clause_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!with_clause_7_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "with_clause_7", c)) break;
    }
    return true;
  }

  // ',' cte_table_name AS '(' compound_select_stmt ')'
  private static boolean with_clause_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_clause_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && cte_table_name(b, l + 1);
    r = r && consumeTokens(b, 0, AS, LP);
    r = r && compound_select_stmt(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: ATOM(raise_expr)
  // 1: ATOM(case_expr)
  // 2: ATOM(exists_expr)
  // 3: ATOM(paren_expr)
  // 4: BINARY(binary_or_expr)
  // 5: BINARY(binary_and_expr)
  // 6: BINARY(between_expr)
  // 7: BINARY(binary_like_expr)
  // 8: BINARY(is_expr)
  // 9: POSTFIX(null_expr)
  // 10: POSTFIX(collate_expr)
  // 11: PREFIX(cast_expr)
  // 12: ATOM(function_expr)
  // 13: POSTFIX(in_expr)
  // 14: BINARY(binary_equality_expr)
  // 15: BINARY(binary_boolean_expr)
  // 16: BINARY(binary_bitwise_expr)
  // 17: BINARY(binary_add_expr)
  // 18: BINARY(binary_mult_expr)
  // 19: BINARY(binary_pipe_expr)
  // 20: PREFIX(unary_expr)
  // 21: ATOM(literal_expr)
  // 22: ATOM(column_expr)
  // 23: ATOM(mybatis_expr)
  // 24: ATOM(keyword_expr)
  // 25: ATOM(bind_expr)
  public static boolean expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = raise_expr(b, l + 1);
    if (!r) r = case_expr(b, l + 1);
    if (!r) r = exists_expr(b, l + 1);
    if (!r) r = paren_expr(b, l + 1);
    if (!r) r = cast_expr(b, l + 1);
    if (!r) r = function_expr(b, l + 1);
    if (!r) r = unary_expr(b, l + 1);
    if (!r) r = literal_expr(b, l + 1);
    if (!r) r = column_expr(b, l + 1);
    if (!r) r = mybatis_expr(b, l + 1);
    if (!r) r = keyword_expr(b, l + 1);
    if (!r) r = bind_expr(b, l + 1);
    p = r;
    r = r && expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 4 && consumeTokenSmart(b, OR)) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, BINARY_OR_EXPR, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, AND)) {
        r = expr(b, l, 5);
        exit_section_(b, l, m, BINARY_AND_EXPR, r, true, null);
      }
      else if (g < 6 && between_expr_0(b, l + 1)) {
        r = report_error_(b, expr(b, l, 6));
        r = between_expr_1(b, l + 1) && r;
        exit_section_(b, l, m, BETWEEN_EXPR, r, true, null);
      }
      else if (g < 7 && binary_like_expr_0(b, l + 1)) {
        r = report_error_(b, expr(b, l, 7));
        r = binary_like_expr_1(b, l + 1) && r;
        exit_section_(b, l, m, BINARY_LIKE_EXPR, r, true, null);
      }
      else if (g < 8 && is_expr_0(b, l + 1)) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, IS_EXPR, r, true, null);
      }
      else if (g < 9 && null_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, NULL_EXPR, r, true, null);
      }
      else if (g < 10 && collate_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, COLLATE_EXPR, r, true, null);
      }
      else if (g < 13 && in_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, IN_EXPR, r, true, null);
      }
      else if (g < 14 && binary_equality_expr_0(b, l + 1)) {
        r = expr(b, l, 14);
        exit_section_(b, l, m, BINARY_EQUALITY_EXPR, r, true, null);
      }
      else if (g < 15 && binary_boolean_expr_0(b, l + 1)) {
        r = expr(b, l, 15);
        exit_section_(b, l, m, BINARY_BOOLEAN_EXPR, r, true, null);
      }
      else if (g < 16 && binary_bitwise_expr_0(b, l + 1)) {
        r = expr(b, l, 16);
        exit_section_(b, l, m, BINARY_BITWISE_EXPR, r, true, null);
      }
      else if (g < 17 && binary_add_expr_0(b, l + 1)) {
        r = expr(b, l, 17);
        exit_section_(b, l, m, BINARY_ADD_EXPR, r, true, null);
      }
      else if (g < 18 && binary_mult_expr_0(b, l + 1)) {
        r = expr(b, l, 18);
        exit_section_(b, l, m, BINARY_MULT_EXPR, r, true, null);
      }
      else if (g < 19 && consumeTokenSmart(b, CONCAT)) {
        r = expr(b, l, 19);
        exit_section_(b, l, m, BINARY_PIPE_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // raise_function
  public static boolean raise_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "raise_expr")) return false;
    if (!nextTokenIsSmart(b, RAISE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = raise_function(b, l + 1);
    exit_section_(b, m, RAISE_EXPR, r);
    return r;
  }

  // CASE [ expr ] WHEN expr THEN expr ( WHEN expr THEN expr )* [ ELSE expr ] END
  public static boolean case_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr")) return false;
    if (!nextTokenIsSmart(b, CASE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CASE_EXPR, null);
    r = consumeTokenSmart(b, CASE);
    p = r; // pin = 1
    r = r && report_error_(b, case_expr_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, WHEN)) && r;
    r = p && report_error_(b, expr(b, l + 1, -1)) && r;
    r = p && report_error_(b, consumeToken(b, THEN)) && r;
    r = p && report_error_(b, expr(b, l + 1, -1)) && r;
    r = p && report_error_(b, case_expr_6(b, l + 1)) && r;
    r = p && report_error_(b, case_expr_7(b, l + 1)) && r;
    r = p && consumeToken(b, END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ expr ]
  private static boolean case_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // ( WHEN expr THEN expr )*
  private static boolean case_expr_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!case_expr_6_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_expr_6", c)) break;
    }
    return true;
  }

  // WHEN expr THEN expr
  private static boolean case_expr_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, WHEN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, THEN);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ELSE expr ]
  private static boolean case_expr_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr_7")) return false;
    case_expr_7_0(b, l + 1);
    return true;
  }

  // ELSE expr
  private static boolean case_expr_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_expr_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ELSE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ [ NOT ] EXISTS ] '(' compound_select_stmt ')'
  public static boolean exists_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXISTS_EXPR, "<exists expr>");
    r = exists_expr_0(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && compound_select_stmt(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ [ NOT ] EXISTS ]
  private static boolean exists_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_expr_0")) return false;
    exists_expr_0_0(b, l + 1);
    return true;
  }

  // [ NOT ] EXISTS
  private static boolean exists_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = exists_expr_0_0_0(b, l + 1);
    r = r && consumeToken(b, EXISTS);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean exists_expr_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_expr_0_0_0")) return false;
    consumeTokenSmart(b, NOT);
    return true;
  }

  // '(' expr ')'
  public static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    if (!nextTokenIsSmart(b, LP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAREN_EXPR, null);
    r = consumeTokenSmart(b, LP);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1, -1));
    r = p && consumeToken(b, RP) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ NOT ] BETWEEN
  private static boolean between_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "between_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = between_expr_0_0(b, l + 1);
    r = r && consumeToken(b, BETWEEN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean between_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "between_expr_0_0")) return false;
    consumeTokenSmart(b, NOT);
    return true;
  }

  // AND expr
  private static boolean between_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "between_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AND);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ] ( binary_like_operator )
  private static boolean binary_like_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = binary_like_expr_0_0(b, l + 1);
    r = r && binary_like_expr_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean binary_like_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_0_0")) return false;
    consumeTokenSmart(b, NOT);
    return true;
  }

  // ( binary_like_operator )
  private static boolean binary_like_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = binary_like_operator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ESCAPE expr ] {
  // }
  private static boolean binary_like_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = binary_like_expr_1_0(b, l + 1);
    r = r && binary_like_expr_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ESCAPE expr ]
  private static boolean binary_like_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_1_0")) return false;
    binary_like_expr_1_0_0(b, l + 1);
    return true;
  }

  // ESCAPE expr
  private static boolean binary_like_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_like_expr_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ESCAPE);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // {
  // }
  private static boolean binary_like_expr_1_1(PsiBuilder b, int l) {
    return true;
  }

  // IS [ NOT ]
  private static boolean is_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "is_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, IS);
    r = r && is_expr_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean is_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "is_expr_0_1")) return false;
    consumeTokenSmart(b, NOT);
    return true;
  }

  // IS NULL | IS NOT NULL
  private static boolean null_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "null_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokensSmart(b, 0, IS, NULL);
    if (!r) r = parseTokensSmart(b, 0, IS, NOT, NULL);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLLATE collation_name
  private static boolean collate_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "collate_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COLLATE);
    r = r && collation_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean cast_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cast_expr")) return false;
    if (!nextTokenIsSmart(b, CAST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = parseTokensSmart(b, 0, CAST, LP);
    p = r;
    r = p && expr(b, l, 11);
    r = p && report_error_(b, cast_expr_1(b, l + 1)) && r;
    exit_section_(b, l, m, CAST_EXPR, r, p, null);
    return r || p;
  }

  // AS type_name ')'
  private static boolean cast_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cast_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AS);
    r = r && type_name(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // function_name '(' [ [ DISTINCT ] expr ( [ ',' ] expr ) * | '*' ] ')'
  public static boolean function_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr")) return false;
    if (!nextTokenIsSmart(b, ID, IF)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_EXPR, "<function expr>");
    r = function_name(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && function_expr_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ [ DISTINCT ] expr ( [ ',' ] expr ) * | '*' ]
  private static boolean function_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2")) return false;
    function_expr_2_0(b, l + 1);
    return true;
  }

  // [ DISTINCT ] expr ( [ ',' ] expr ) * | '*'
  private static boolean function_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_expr_2_0_0(b, l + 1);
    if (!r) r = consumeTokenSmart(b, MULTIPLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ DISTINCT ] expr ( [ ',' ] expr ) *
  private static boolean function_expr_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_expr_2_0_0_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    r = r && function_expr_2_0_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ DISTINCT ]
  private static boolean function_expr_2_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0_0_0")) return false;
    consumeTokenSmart(b, DISTINCT);
    return true;
  }

  // ( [ ',' ] expr ) *
  private static boolean function_expr_2_0_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_expr_2_0_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_expr_2_0_0_2", c)) break;
    }
    return true;
  }

  // [ ',' ] expr
  private static boolean function_expr_2_0_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_expr_2_0_0_2_0_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ',' ]
  private static boolean function_expr_2_0_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_expr_2_0_0_2_0_0")) return false;
    consumeTokenSmart(b, COMMA);
    return true;
  }

  // [ NOT ] IN ( '(' [ compound_select_stmt | expr ( ',' expr ) * ] ')' |
  //     [ database_name '.' ] table_name | bind_expr | mybatis_expr )
  private static boolean in_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = in_expr_0_0(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && in_expr_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ NOT ]
  private static boolean in_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_0")) return false;
    consumeTokenSmart(b, NOT);
    return true;
  }

  // '(' [ compound_select_stmt | expr ( ',' expr ) * ] ')' |
  //     [ database_name '.' ] table_name | bind_expr | mybatis_expr
  private static boolean in_expr_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = in_expr_0_2_0(b, l + 1);
    if (!r) r = in_expr_0_2_1(b, l + 1);
    if (!r) r = bind_expr(b, l + 1);
    if (!r) r = mybatis_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' [ compound_select_stmt | expr ( ',' expr ) * ] ')'
  private static boolean in_expr_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LP);
    r = r && in_expr_0_2_0_1(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ compound_select_stmt | expr ( ',' expr ) * ]
  private static boolean in_expr_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0_1")) return false;
    in_expr_0_2_0_1_0(b, l + 1);
    return true;
  }

  // compound_select_stmt | expr ( ',' expr ) *
  private static boolean in_expr_0_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compound_select_stmt(b, l + 1);
    if (!r) r = in_expr_0_2_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ( ',' expr ) *
  private static boolean in_expr_0_2_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1, -1);
    r = r && in_expr_0_2_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' expr ) *
  private static boolean in_expr_0_2_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0_1_0_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!in_expr_0_2_0_1_0_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "in_expr_0_2_0_1_0_1_1", c)) break;
    }
    return true;
  }

  // ',' expr
  private static boolean in_expr_0_2_0_1_0_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_0_1_0_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ] table_name
  private static boolean in_expr_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = in_expr_0_2_1_0(b, l + 1);
    r = r && table_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ database_name '.' ]
  private static boolean in_expr_0_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_1_0")) return false;
    in_expr_0_2_1_0_0(b, l + 1);
    return true;
  }

  // database_name '.'
  private static boolean in_expr_0_2_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "in_expr_0_2_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' | '==' | '!=' | '<>'
  private static boolean binary_equality_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_equality_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, EQ);
    if (!r) r = consumeTokenSmart(b, EQ2);
    if (!r) r = consumeTokenSmart(b, NEQ);
    if (!r) r = consumeTokenSmart(b, NEQ2);
    return r;
  }

  // '<' | '<=' | '>' | '>='
  private static boolean binary_boolean_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_boolean_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, LT);
    if (!r) r = consumeTokenSmart(b, LTE);
    if (!r) r = consumeTokenSmart(b, GT);
    if (!r) r = consumeTokenSmart(b, GTE);
    return r;
  }

  // '<<' |  '>>' | '&' | '|'
  private static boolean binary_bitwise_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_bitwise_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, SHIFT_LEFT);
    if (!r) r = consumeTokenSmart(b, SHIFT_RIGHT);
    if (!r) r = consumeTokenSmart(b, BITWISE_AND);
    if (!r) r = consumeTokenSmart(b, BITWISE_OR);
    return r;
  }

  // '+' | '-'
  private static boolean binary_add_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_add_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, PLUS);
    if (!r) r = consumeTokenSmart(b, MINUS);
    return r;
  }

  // '*' | '/' | '%'
  private static boolean binary_mult_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_mult_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, MULTIPLY);
    if (!r) r = consumeTokenSmart(b, DIVIDE);
    if (!r) r = consumeTokenSmart(b, MOD);
    return r;
  }

  public static boolean unary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = unary_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 20);
    exit_section_(b, l, m, UNARY_EXPR, r, p, null);
    return r || p;
  }

  // '+' | '-' | '~'
  private static boolean unary_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, PLUS);
    if (!r) r = consumeTokenSmart(b, MINUS);
    if (!r) r = consumeTokenSmart(b, BITWISE_NOT);
    return r;
  }

  // literal_value
  public static boolean literal_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_EXPR, "<literal expr>");
    r = literal_value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ database_name '.' table_name '.' | table_name '.' ] column_name
  public static boolean column_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_expr")) return false;
    if (!nextTokenIsSmart(b, ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLUMN_EXPR, "<column expr>");
    r = column_expr_0(b, l + 1);
    r = r && column_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ database_name '.' table_name '.' | table_name '.' ]
  private static boolean column_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_expr_0")) return false;
    column_expr_0_0(b, l + 1);
    return true;
  }

  // database_name '.' table_name '.' | table_name '.'
  private static boolean column_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = column_expr_0_0_0(b, l + 1);
    if (!r) r = column_expr_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // database_name '.' table_name '.'
  private static boolean column_expr_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_expr_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = database_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    r = r && table_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // table_name '.'
  private static boolean column_expr_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "column_expr_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_name(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // MYBATIS_OGNL
  public static boolean mybatis_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mybatis_expr")) return false;
    if (!nextTokenIsSmart(b, MYBATIS_OGNL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, MYBATIS_OGNL);
    exit_section_(b, m, MYBATIS_EXPR, r);
    return r;
  }

  // MONTH | DAY | HOUR | MINUTE | SEPARATOR | INTERVAL
  public static boolean keyword_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "keyword_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KEYWORD_EXPR, "<keyword expr>");
    r = consumeTokenSmart(b, MONTH);
    if (!r) r = consumeTokenSmart(b, DAY);
    if (!r) r = consumeTokenSmart(b, HOUR);
    if (!r) r = consumeTokenSmart(b, MINUTE);
    if (!r) r = consumeTokenSmart(b, SEPARATOR);
    if (!r) r = consumeTokenSmart(b, INTERVAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // bind_parameter
  public static boolean bind_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bind_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BIND_EXPR, "<bind expr>");
    r = bind_parameter(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
