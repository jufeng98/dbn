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

public class SqlCreateTableStmtImpl extends SqlPsiElement implements SqlCreateTableStmt {

  public SqlCreateTableStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitCreateTableStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlColumnDef> getColumnDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlColumnDef.class);
  }

  @Override
  @Nullable
  public SqlCompoundSelectStmt getCompoundSelectStmt() {
    return findChildByClass(SqlCompoundSelectStmt.class);
  }

  @Override
  @Nullable
  public SqlDatabaseName getDatabaseName() {
    return findChildByClass(SqlDatabaseName.class);
  }

  @Override
  @NotNull
  public List<SqlTableAttr> getTableAttrList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlTableAttr.class);
  }

  @Override
  @NotNull
  public List<SqlTableConstraint> getTableConstraintList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlTableConstraint.class);
  }

  @Override
  @NotNull
  public SqlTableName getTableName() {
    return findNotNullChildByClass(SqlTableName.class);
  }

  @Override
  @Nullable
  public PsiElement getAs() {
    return findChildByType(AS);
  }

  @Override
  @NotNull
  public PsiElement getCreate() {
    return findNotNullChildByType(CREATE);
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
  @Nullable
  public PsiElement getNot() {
    return findChildByType(NOT);
  }

  @Override
  @NotNull
  public PsiElement getTable() {
    return findNotNullChildByType(TABLE);
  }

  @Override
  @Nullable
  public PsiElement getTemp() {
    return findChildByType(TEMP);
  }

  @Override
  @Nullable
  public PsiElement getTemporary() {
    return findChildByType(TEMPORARY);
  }

  @Override
  @Nullable
  public PsiElement getWithout() {
    return findChildByType(WITHOUT);
  }

}
