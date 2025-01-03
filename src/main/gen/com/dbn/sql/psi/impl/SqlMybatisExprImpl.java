// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dbn.sql.psi.SqlTypes.*;
import com.dbn.sql.inject.SqlPsiLanguageInjectionHost;
import com.dbn.sql.psi.*;
import com.intellij.psi.PsiReference;

public class SqlMybatisExprImpl extends SqlPsiLanguageInjectionHost implements SqlMybatisExpr {

  public SqlMybatisExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitMybatisExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getMybatisOgnl() {
    return findNotNullChildByType(MYBATIS_OGNL);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return SqlPsiImplUtil.getReferences(this);
  }

}
