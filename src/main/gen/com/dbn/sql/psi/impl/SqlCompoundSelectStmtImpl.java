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

public class SqlCompoundSelectStmtImpl extends SqlPsiElement implements SqlCompoundSelectStmt {

  public SqlCompoundSelectStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitCompoundSelectStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlCompoundOperator> getCompoundOperatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlCompoundOperator.class);
  }

  @Override
  @NotNull
  public List<SqlLimitingTerm> getLimitingTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlLimitingTerm.class);
  }

  @Override
  @NotNull
  public List<SqlOrderingTerm> getOrderingTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlOrderingTerm.class);
  }

  @Override
  @NotNull
  public List<SqlSelectStmt> getSelectStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlSelectStmt.class);
  }

  @Override
  @Nullable
  public SqlWithClause getWithClause() {
    return findChildByClass(SqlWithClause.class);
  }

  @Override
  @Nullable
  public PsiElement getBy() {
    return findChildByType(BY);
  }

  @Override
  @Nullable
  public PsiElement getLimit() {
    return findChildByType(LIMIT);
  }

  @Override
  @Nullable
  public PsiElement getOffset() {
    return findChildByType(OFFSET);
  }

  @Override
  @Nullable
  public PsiElement getOrder() {
    return findChildByType(ORDER);
  }

}
