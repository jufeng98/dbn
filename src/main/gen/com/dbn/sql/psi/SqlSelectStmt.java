// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlSelectStmt extends PsiElement {

  @Nullable
  SqlCompoundResultColumn getCompoundResultColumn();

  @Nullable
  SqlExpr getExpr();

  @Nullable
  SqlGroupingTerm getGroupingTerm();

  @Nullable
  SqlJoinClause getJoinClause();

  @NotNull
  List<SqlOrderingTerm> getOrderingTermList();

  @NotNull
  List<SqlValuesExpression> getValuesExpressionList();

  @Nullable
  PsiElement getAll();

  @Nullable
  PsiElement getDistinct();

  @Nullable
  PsiElement getFrom();

  @Nullable
  PsiElement getGroup();

  @Nullable
  PsiElement getOrder();

  @Nullable
  PsiElement getSelect();

  @Nullable
  PsiElement getValues();

  @Nullable
  PsiElement getWhere();

}
