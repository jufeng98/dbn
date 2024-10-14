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

public class SqlTableOrSubqueryImpl extends SqlPsiElement implements SqlTableOrSubquery {

  public SqlTableOrSubqueryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitTableOrSubquery(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
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
  @Nullable
  public SqlIndexName getIndexName() {
    return findChildByClass(SqlIndexName.class);
  }

  @Override
  @Nullable
  public SqlJoinClause getJoinClause() {
    return findChildByClass(SqlJoinClause.class);
  }

  @Override
  @Nullable
  public SqlTableAlias getTableAlias() {
    return findChildByClass(SqlTableAlias.class);
  }

  @Override
  @Nullable
  public SqlTableName getTableName() {
    return findChildByClass(SqlTableName.class);
  }

  @Override
  @NotNull
  public List<SqlTableOrSubquery> getTableOrSubqueryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlTableOrSubquery.class);
  }

  @Override
  @Nullable
  public PsiElement getAs() {
    return findChildByType(AS);
  }

  @Override
  @Nullable
  public PsiElement getBy() {
    return findChildByType(BY);
  }

  @Override
  @Nullable
  public PsiElement getIndexed() {
    return findChildByType(INDEXED);
  }

  @Override
  @Nullable
  public PsiElement getNot() {
    return findChildByType(NOT);
  }

}
