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

public class SqlAlterTableStmtImpl extends SqlPsiElement implements SqlAlterTableStmt {

  public SqlAlterTableStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitAlterTableStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlColumnDef getColumnDef() {
    return findChildByClass(SqlColumnDef.class);
  }

  @Override
  @Nullable
  public SqlDatabaseName getDatabaseName() {
    return findChildByClass(SqlDatabaseName.class);
  }

  @Override
  @Nullable
  public SqlNewTableName getNewTableName() {
    return findChildByClass(SqlNewTableName.class);
  }

  @Override
  @Nullable
  public SqlTableName getTableName() {
    return findChildByClass(SqlTableName.class);
  }

  @Override
  @Nullable
  public PsiElement getAdd() {
    return findChildByType(ADD);
  }

  @Override
  @NotNull
  public PsiElement getAlter() {
    return findNotNullChildByType(ALTER);
  }

  @Override
  @Nullable
  public PsiElement getColumn() {
    return findChildByType(COLUMN);
  }

  @Override
  @Nullable
  public PsiElement getRename() {
    return findChildByType(RENAME);
  }

  @Override
  @Nullable
  public PsiElement getTable() {
    return findChildByType(TABLE);
  }

  @Override
  @Nullable
  public PsiElement getTo() {
    return findChildByType(TO);
  }

}
