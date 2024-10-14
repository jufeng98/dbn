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

public class SqlConflictClauseImpl extends SqlPsiElement implements SqlConflictClause {

  public SqlConflictClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitConflictClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlIdentifier getIdentifier() {
    return findChildByClass(SqlIdentifier.class);
  }

  @Override
  @Nullable
  public PsiElement getAbort() {
    return findChildByType(ABORT);
  }

  @Override
  @Nullable
  public PsiElement getFail() {
    return findChildByType(FAIL);
  }

  @Override
  @Nullable
  public PsiElement getIgnore() {
    return findChildByType(IGNORE);
  }

  @Override
  @Nullable
  public PsiElement getOn() {
    return findChildByType(ON);
  }

  @Override
  @Nullable
  public PsiElement getRollback() {
    return findChildByType(ROLLBACK);
  }

  @Override
  @Nullable
  public PsiElement getUsing() {
    return findChildByType(USING);
  }

}
