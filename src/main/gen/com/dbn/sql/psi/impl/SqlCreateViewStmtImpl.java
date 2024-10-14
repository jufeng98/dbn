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

public class SqlCreateViewStmtImpl extends SqlPsiElement implements SqlCreateViewStmt {

  public SqlCreateViewStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitCreateViewStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SqlColumnAlias> getColumnAliasList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlColumnAlias.class);
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
  public SqlViewName getViewName() {
    return findNotNullChildByClass(SqlViewName.class);
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
  @NotNull
  public PsiElement getView() {
    return findNotNullChildByType(VIEW);
  }

}
