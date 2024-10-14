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

public class SqlSelectStmtImpl extends SqlPsiElement implements SqlSelectStmt {

  public SqlSelectStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitSelectStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlCompoundResultColumn getCompoundResultColumn() {
    return findChildByClass(SqlCompoundResultColumn.class);
  }

  @Override
  @Nullable
  public SqlExpr getExpr() {
    return findChildByClass(SqlExpr.class);
  }

  @Override
  @Nullable
  public SqlGroupingTerm getGroupingTerm() {
    return findChildByClass(SqlGroupingTerm.class);
  }

  @Override
  @Nullable
  public SqlJoinClause getJoinClause() {
    return findChildByClass(SqlJoinClause.class);
  }

  @Override
  @NotNull
  public List<SqlOrderingTerm> getOrderingTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlOrderingTerm.class);
  }

  @Override
  @NotNull
  public List<SqlValuesExpression> getValuesExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlValuesExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getAll() {
    return findChildByType(ALL);
  }

  @Override
  @Nullable
  public PsiElement getDistinct() {
    return findChildByType(DISTINCT);
  }

  @Override
  @Nullable
  public PsiElement getFrom() {
    return findChildByType(FROM);
  }

  @Override
  @Nullable
  public PsiElement getGroup() {
    return findChildByType(GROUP);
  }

  @Override
  @Nullable
  public PsiElement getOrder() {
    return findChildByType(ORDER);
  }

  @Override
  @Nullable
  public PsiElement getSelect() {
    return findChildByType(SELECT);
  }

  @Override
  @Nullable
  public PsiElement getValues() {
    return findChildByType(VALUES);
  }

  @Override
  @Nullable
  public PsiElement getWhere() {
    return findChildByType(WHERE);
  }

}
