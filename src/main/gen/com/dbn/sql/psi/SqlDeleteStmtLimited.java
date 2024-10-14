// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlDeleteStmtLimited extends PsiElement {

  @NotNull
  List<SqlExpr> getExprList();

  @NotNull
  List<SqlOrderingTerm> getOrderingTermList();

  @NotNull
  SqlQualifiedTableName getQualifiedTableName();

  @Nullable
  SqlWithClause getWithClause();

  @Nullable
  PsiElement getBy();

  @NotNull
  PsiElement getDelete();

  @NotNull
  PsiElement getFrom();

  @Nullable
  PsiElement getLimit();

  @Nullable
  PsiElement getOffset();

  @Nullable
  PsiElement getOrder();

  @Nullable
  PsiElement getWhere();

}
