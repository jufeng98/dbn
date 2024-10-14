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

public class SqlCreateTriggerStmtImpl extends SqlPsiElement implements SqlCreateTriggerStmt {

  public SqlCreateTriggerStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitCreateTriggerStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlColumnName> getColumnNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlColumnName.class);
  }

  @Override
  @NotNull
  public List<SqlCompoundSelectStmt> getCompoundSelectStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlCompoundSelectStmt.class);
  }

  @Override
  @Nullable
  public SqlDatabaseName getDatabaseName() {
    return findChildByClass(SqlDatabaseName.class);
  }

  @Override
  @NotNull
  public List<SqlDeleteStmt> getDeleteStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlDeleteStmt.class);
  }

  @Override
  @Nullable
  public SqlExpr getExpr() {
    return findChildByClass(SqlExpr.class);
  }

  @Override
  @NotNull
  public List<SqlInsertStmt> getInsertStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlInsertStmt.class);
  }

  @Override
  @Nullable
  public SqlTableName getTableName() {
    return findChildByClass(SqlTableName.class);
  }

  @Override
  @NotNull
  public SqlTriggerName getTriggerName() {
    return findNotNullChildByClass(SqlTriggerName.class);
  }

  @Override
  @NotNull
  public List<SqlUpdateStmt> getUpdateStmtList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlUpdateStmt.class);
  }

  @Override
  @Nullable
  public PsiElement getBegin() {
    return findChildByType(BEGIN);
  }

  @Override
  @NotNull
  public PsiElement getCreate() {
    return findNotNullChildByType(CREATE);
  }

  @Override
  @Nullable
  public PsiElement getDelete() {
    return findChildByType(DELETE);
  }

  @Override
  @Nullable
  public PsiElement getEnd() {
    return findChildByType(END);
  }

  @Override
  @Nullable
  public PsiElement getExists() {
    return findChildByType(EXISTS);
  }

  @Override
  @Nullable
  public PsiElement getFor() {
    return findChildByType(FOR);
  }

  @Override
  @Nullable
  public PsiElement getIf() {
    return findChildByType(IF);
  }

  @Override
  @Nullable
  public PsiElement getInsert() {
    return findChildByType(INSERT);
  }

  @Override
  @Nullable
  public PsiElement getNot() {
    return findChildByType(NOT);
  }

  @Override
  @Nullable
  public PsiElement getOn() {
    return findChildByType(ON);
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
  public PsiElement getUpdate() {
    return findChildByType(UPDATE);
  }

  @Override
  @Nullable
  public PsiElement getWhen() {
    return findChildByType(WHEN);
  }

}
