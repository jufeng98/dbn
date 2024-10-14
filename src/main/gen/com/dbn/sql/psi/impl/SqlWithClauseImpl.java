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

public class SqlWithClauseImpl extends SqlPsiElement implements SqlWithClause {

  public SqlWithClauseImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitWithClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlCompoundSelectStmt> getCompoundSelectStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlCompoundSelectStmt.class);
  }

  @Override
  @NotNull
  public List<SqlCteTableName> getCteTableNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlCteTableName.class);
  }

  @Override
  @Nullable
  public PsiElement getRecursive() {
    return findChildByType(RECURSIVE);
  }

  @Override
  @NotNull
  public PsiElement getWith() {
    return findNotNullChildByType(WITH);
  }

}
