// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlCompoundSelectStmt extends PsiElement {

  @NotNull
  List<SqlCompoundOperator> getCompoundOperatorList();

  @NotNull
  List<SqlLimitingTerm> getLimitingTermList();

  @NotNull
  List<SqlOrderingTerm> getOrderingTermList();

  @NotNull
  List<SqlSelectStmt> getSelectStmtList();

  @Nullable
  SqlWithClause getWithClause();

  @Nullable
  PsiElement getBy();

  @Nullable
  PsiElement getLimit();

  @Nullable
  PsiElement getOffset();

  @Nullable
  PsiElement getOrder();

}
