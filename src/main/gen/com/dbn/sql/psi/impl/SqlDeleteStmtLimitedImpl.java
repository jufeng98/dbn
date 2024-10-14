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

public class SqlDeleteStmtLimitedImpl extends SqlPsiElement implements SqlDeleteStmtLimited {

  public SqlDeleteStmtLimitedImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitDeleteStmtLimited(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlExpr.class);
  }

  @Override
  @NotNull
  public List<SqlOrderingTerm> getOrderingTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlOrderingTerm.class);
  }

  @Override
  @NotNull
  public SqlQualifiedTableName getQualifiedTableName() {
    return findNotNullChildByClass(SqlQualifiedTableName.class);
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
  @NotNull
  public PsiElement getDelete() {
    return findNotNullChildByType(DELETE);
  }

  @Override
  @NotNull
  public PsiElement getFrom() {
    return findNotNullChildByType(FROM);
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

  @Override
  @Nullable
  public PsiElement getWhere() {
    return findChildByType(WHERE);
  }

}
