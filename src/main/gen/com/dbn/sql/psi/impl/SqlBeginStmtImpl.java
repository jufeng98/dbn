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

public class SqlBeginStmtImpl extends SqlPsiElement implements SqlBeginStmt {

  public SqlBeginStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitBeginStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getBegin() {
    return findNotNullChildByType(BEGIN);
  }

  @Override
  @Nullable
  public PsiElement getDeferred() {
    return findChildByType(DEFERRED);
  }

  @Override
  @Nullable
  public PsiElement getExclusive() {
    return findChildByType(EXCLUSIVE);
  }

  @Override
  @Nullable
  public PsiElement getImmediate() {
    return findChildByType(IMMEDIATE);
  }

  @Override
  @Nullable
  public PsiElement getTransaction() {
    return findChildByType(TRANSACTION);
  }

}