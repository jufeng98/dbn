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

public class SqlJoinConstraintImpl extends SqlPsiElement implements SqlJoinConstraint {

  public SqlJoinConstraintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitJoinConstraint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlColumnName> getColumnNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlColumnName.class);
  }

  @Override
  @Nullable
  public SqlExpr getExpr() {
    return findChildByClass(SqlExpr.class);
  }

  @Override
  @Nullable
  public PsiElement getOn() {
    return findChildByType(ON);
  }

  @Override
  @Nullable
  public PsiElement getUsing() {
    return findChildByType(USING);
  }

}
