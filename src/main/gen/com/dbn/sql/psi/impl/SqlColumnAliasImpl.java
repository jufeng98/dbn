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

public class SqlColumnAliasImpl extends SqlNamedElementImpl implements SqlColumnAlias {

  public SqlColumnAliasImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitColumnAlias(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ID);
  }

  @Override
  @NotNull
  public PsiElement setName(@Nullable String newName) {
    return SqlPsiImplUtil.setName(this, newName);
  }

  @Override
  @NotNull
  public String getName() {
    return SqlPsiImplUtil.getName(this);
  }

  @Override
  @NotNull
  public PsiElement getNameIdentifier() {
    return SqlPsiImplUtil.getNameIdentifier(this);
  }

}
