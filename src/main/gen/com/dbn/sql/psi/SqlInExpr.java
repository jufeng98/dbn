// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlInExpr extends SqlExpr {

  @Nullable
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @NotNull
  List<SqlExpr> getExprList();

  @Nullable
  SqlTableName getTableName();

  @NotNull
  PsiElement getIn();

  @Nullable
  PsiElement getNot();

}
