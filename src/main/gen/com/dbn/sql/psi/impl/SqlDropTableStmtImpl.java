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

public class SqlDropTableStmtImpl extends SqlPsiElement implements SqlDropTableStmt {

  public SqlDropTableStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitDropTableStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlDatabaseName getDatabaseName() {
    return findChildByClass(SqlDatabaseName.class);
  }

  @Override
  @Nullable
  public SqlTableName getTableName() {
    return findChildByClass(SqlTableName.class);
  }

  @Override
  @NotNull
  public PsiElement getDrop() {
    return findNotNullChildByType(DROP);
  }

  @Override
  @Nullable
  public PsiElement getExists() {
    return findChildByType(EXISTS);
  }

  @Override
  @Nullable
  public PsiElement getIf() {
    return findChildByType(IF);
  }

  @Override
  @NotNull
  public PsiElement getTable() {
    return findNotNullChildByType(TABLE);
  }

}
