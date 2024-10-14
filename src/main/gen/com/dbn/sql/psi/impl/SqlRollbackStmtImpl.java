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

public class SqlRollbackStmtImpl extends SqlPsiElement implements SqlRollbackStmt {

  public SqlRollbackStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitRollbackStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlSavepointName getSavepointName() {
    return findChildByClass(SqlSavepointName.class);
  }

  @Override
  @NotNull
  public PsiElement getRollback() {
    return findNotNullChildByType(ROLLBACK);
  }

  @Override
  @Nullable
  public PsiElement getSavepoint() {
    return findChildByType(SAVEPOINT);
  }

  @Override
  @Nullable
  public PsiElement getTo() {
    return findChildByType(TO);
  }

  @Override
  @Nullable
  public PsiElement getTransaction() {
    return findChildByType(TRANSACTION);
  }

}
