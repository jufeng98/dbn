// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dbn.sql.psi.SqlTypes.*;
import com.dbn.sql.psi.*;

public class SqlRaiseExprImpl extends SqlExprImpl implements SqlRaiseExpr {

  public SqlRaiseExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitRaiseExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SqlRaiseFunction getRaiseFunction() {
    return findNotNullChildByClass(SqlRaiseFunction.class);
  }

}
