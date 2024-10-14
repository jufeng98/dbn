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

public class SqlLiteralValueImpl extends SqlPsiElement implements SqlLiteralValue {

  public SqlLiteralValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitLiteralValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlBlobLiteral getBlobLiteral() {
    return findChildByClass(SqlBlobLiteral.class);
  }

  @Override
  @Nullable
  public SqlNumericLiteral getNumericLiteral() {
    return findChildByClass(SqlNumericLiteral.class);
  }

  @Override
  @Nullable
  public SqlStringLiteral getStringLiteral() {
    return findChildByClass(SqlStringLiteral.class);
  }

  @Override
  @Nullable
  public PsiElement getCurrentDate() {
    return findChildByType(CURRENT_DATE);
  }

  @Override
  @Nullable
  public PsiElement getCurrentTime() {
    return findChildByType(CURRENT_TIME);
  }

  @Override
  @Nullable
  public PsiElement getCurrentTimestamp() {
    return findChildByType(CURRENT_TIMESTAMP);
  }

  @Override
  @Nullable
  public PsiElement getNull() {
    return findChildByType(NULL);
  }

}
