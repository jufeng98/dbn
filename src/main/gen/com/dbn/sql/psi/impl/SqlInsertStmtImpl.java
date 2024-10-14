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

public class SqlInsertStmtImpl extends SqlPsiElement implements SqlInsertStmt {

  public SqlInsertStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitInsertStmt(this);
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
  public SqlTableName getTableName() {
    return findNotNullChildByClass(SqlTableName.class);
  }

  @Override
  @NotNull
  public List<SqlValuesExpression> getValuesExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlValuesExpression.class);
  }

  @Override
  @Nullable
  public SqlWithClause getWithClause() {
    return findChildByClass(SqlWithClause.class);
  }

  @Override
  @Nullable
  public PsiElement getAbort() {
    return findChildByType(ABORT);
  }

  @Override
  @Nullable
  public PsiElement getDefault() {
    return findChildByType(DEFAULT);
  }

  @Override
  @Nullable
  public PsiElement getFail() {
    return findChildByType(FAIL);
  }

  @Override
  @Nullable
  public PsiElement getIgnore() {
    return findChildByType(IGNORE);
  }

  @Override
  @Nullable
  public PsiElement getInsert() {
    return findChildByType(INSERT);
  }

  @Override
  @NotNull
  public PsiElement getInto() {
    return findNotNullChildByType(INTO);
  }

  @Override
  @Nullable
  public PsiElement getOr() {
    return findChildByType(OR);
  }

  @Override
  @Nullable
  public PsiElement getRollback() {
    return findChildByType(ROLLBACK);
  }

  @Override
  @Nullable
  public PsiElement getValues() {
    return findChildByType(VALUES);
  }

}
