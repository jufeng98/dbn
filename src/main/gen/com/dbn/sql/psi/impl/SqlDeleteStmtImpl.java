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

public class SqlDeleteStmtImpl extends SqlPsiElement implements SqlDeleteStmt {

  public SqlDeleteStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitDeleteStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlExpr getExpr() {
    return findChildByClass(SqlExpr.class);
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
  public PsiElement getWhere() {
    return findChildByType(WHERE);
  }

}
