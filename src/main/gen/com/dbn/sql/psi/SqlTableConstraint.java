// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlTableConstraint extends PsiElement {

  @NotNull
  List<SqlColumnName> getColumnNameList();

  @Nullable
  SqlConflictClause getConflictClause();

  @Nullable
  SqlExpr getExpr();

  @Nullable
  SqlForeignKeyClause getForeignKeyClause();

  @Nullable
  SqlIdentifier getIdentifier();

  @Nullable
  SqlIndexName getIndexName();

  @NotNull
  List<SqlIndexedColumn> getIndexedColumnList();

  @Nullable
  PsiElement getCheck();

  @Nullable
  PsiElement getConstraint();

  @Nullable
  PsiElement getForeign();

  @Nullable
  PsiElement getKey();

  @Nullable
  PsiElement getPrimary();

  @Nullable
  PsiElement getUnique();

}
