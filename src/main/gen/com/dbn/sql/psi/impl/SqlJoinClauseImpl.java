// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dbn.sql.psi.SqlTypes.*;
import com.dbn.sql.SqlPsiElement;
import com.dbn.sql.psi.*;

public class SqlJoinClauseImpl extends SqlPsiElement implements SqlJoinClause {

  public SqlJoinClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitJoinClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlJoinConstraint> getJoinConstraintList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlJoinConstraint.class);
  }

  @Override
  @NotNull
  public List<SqlJoinOperator> getJoinOperatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlJoinOperator.class);
  }

  @Override
  @NotNull
  public List<SqlTableOrSubquery> getTableOrSubqueryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlTableOrSubquery.class);
  }

}
