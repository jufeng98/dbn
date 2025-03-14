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

public class SqlIndexedColumnImpl extends SqlPsiElement implements SqlIndexedColumn {

  public SqlIndexedColumnImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitIndexedColumn(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlCollationName getCollationName() {
    return findChildByClass(SqlCollationName.class);
  }

  @Override
  @NotNull
  public SqlColumnName getColumnName() {
    return findNotNullChildByClass(SqlColumnName.class);
  }

  @Override
  @Nullable
  public PsiElement getAsc() {
    return findChildByType(ASC);
  }

  @Override
  @Nullable
  public PsiElement getCollate() {
    return findChildByType(COLLATE);
  }

  @Override
  @Nullable
  public PsiElement getDesc() {
    return findChildByType(DESC);
  }

}
