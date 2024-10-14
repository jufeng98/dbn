// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlCreateTriggerStmt extends PsiElement {

  @NotNull
  List<SqlColumnName> getColumnNameList();

  @NotNull
  List<SqlCompoundSelectStmt> getCompoundSelectStmtList();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @NotNull
  List<SqlDeleteStmt> getDeleteStmtList();

  @Nullable
  SqlExpr getExpr();

  @NotNull
  List<SqlInsertStmt> getInsertStmtList();

  @Nullable
  SqlTableName getTableName();

  @NotNull
  SqlTriggerName getTriggerName();

  @NotNull
  List<SqlUpdateStmt> getUpdateStmtList();

  @Nullable
  PsiElement getBegin();

  @NotNull
  PsiElement getCreate();

  @Nullable
  PsiElement getDelete();

  @Nullable
  PsiElement getEnd();

  @Nullable
  PsiElement getExists();

  @Nullable
  PsiElement getFor();

  @Nullable
  PsiElement getIf();

  @Nullable
  PsiElement getInsert();

  @Nullable
  PsiElement getNot();

  @Nullable
  PsiElement getOn();

  @Nullable
  PsiElement getTemp();

  @Nullable
  PsiElement getTemporary();

  @Nullable
  PsiElement getUpdate();

  @Nullable
  PsiElement getWhen();

}
