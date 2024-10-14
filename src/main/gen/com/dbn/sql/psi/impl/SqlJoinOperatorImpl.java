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

public class SqlJoinOperatorImpl extends SqlPsiElement implements SqlJoinOperator {

  public SqlJoinOperatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitJoinOperator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getCross() {
    return findChildByType(CROSS);
  }

  @Override
  @Nullable
  public PsiElement getInner() {
    return findChildByType(INNER);
  }

  @Override
  @Nullable
  public PsiElement getJoin() {
    return findChildByType(JOIN);
  }

  @Override
  @Nullable
  public PsiElement getLeft() {
    return findChildByType(LEFT);
  }

  @Override
  @Nullable
  public PsiElement getNatural() {
    return findChildByType(NATURAL);
  }

  @Override
  @Nullable
  public PsiElement getOuter() {
    return findChildByType(OUTER);
  }

}
