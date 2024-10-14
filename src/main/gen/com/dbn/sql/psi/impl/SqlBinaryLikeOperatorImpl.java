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

public class SqlBinaryLikeOperatorImpl extends SqlPsiElement implements SqlBinaryLikeOperator {

  public SqlBinaryLikeOperatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitBinaryLikeOperator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getGlob() {
    return findChildByType(GLOB);
  }

  @Override
  @Nullable
  public PsiElement getLike() {
    return findChildByType(LIKE);
  }

  @Override
  @Nullable
  public PsiElement getMatch() {
    return findChildByType(MATCH);
  }

  @Override
  @Nullable
  public PsiElement getRegexp() {
    return findChildByType(REGEXP);
  }

}
