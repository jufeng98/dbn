// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlInsertStmt extends PsiElement {

  @NotNull
  List<SqlColumnName> getColumnNameList();

  @Nullable
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @NotNull
  SqlTableName getTableName();

  @NotNull
  List<SqlValuesExpression> getValuesExpressionList();

  @Nullable
  SqlWithClause getWithClause();

  @Nullable
  PsiElement getAbort();

  @Nullable
  PsiElement getDefault();

  @Nullable
  PsiElement getFail();

  @Nullable
  PsiElement getIgnore();

  @Nullable
  PsiElement getInsert();

  @NotNull
  PsiElement getInto();

  @Nullable
  PsiElement getOr();

  @Nullable
  PsiElement getRollback();

  @Nullable
  PsiElement getValues();

}
