// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlUpdateStmtLimited extends PsiElement {

  @Nullable
  SqlColumnName getColumnName();

  @NotNull
  List<SqlExpr> getExprList();

  @NotNull
  List<SqlOrderingTerm> getOrderingTermList();

  @NotNull
  SqlQualifiedTableName getQualifiedTableName();

  @Nullable
  SqlSetterExpression getSetterExpression();

  @NotNull
  List<SqlUpdateStmtSubsequentSetter> getUpdateStmtSubsequentSetterList();

  @Nullable
  SqlWithClause getWithClause();

  @Nullable
  PsiElement getAbort();

  @Nullable
  PsiElement getBy();

  @Nullable
  PsiElement getFail();

  @Nullable
  PsiElement getIgnore();

  @Nullable
  PsiElement getLimit();

  @Nullable
  PsiElement getOffset();

  @Nullable
  PsiElement getOr();

  @Nullable
  PsiElement getOrder();

  @Nullable
  PsiElement getRollback();

  @Nullable
  PsiElement getSet();

  @NotNull
  PsiElement getUpdate();

  @Nullable
  PsiElement getWhere();

}
