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

public class SqlTableConstraintImpl extends SqlPsiElement implements SqlTableConstraint {

  public SqlTableConstraintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitTableConstraint(this);
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
  public SqlConflictClause getConflictClause() {
    return findChildByClass(SqlConflictClause.class);
  }

  @Override
  @Nullable
  public SqlExpr getExpr() {
    return findChildByClass(SqlExpr.class);
  }

  @Override
  @Nullable
  public SqlForeignKeyClause getForeignKeyClause() {
    return findChildByClass(SqlForeignKeyClause.class);
  }

  @Override
  @Nullable
  public SqlIdentifier getIdentifier() {
    return findChildByClass(SqlIdentifier.class);
  }

  @Override
  @Nullable
  public SqlIndexName getIndexName() {
    return findChildByClass(SqlIndexName.class);
  }

  @Override
  @NotNull
  public List<SqlIndexedColumn> getIndexedColumnList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SqlIndexedColumn.class);
  }

  @Override
  @Nullable
  public PsiElement getCheck() {
    return findChildByType(CHECK);
  }

  @Override
  @Nullable
  public PsiElement getConstraint() {
    return findChildByType(CONSTRAINT);
  }

  @Override
  @Nullable
  public PsiElement getForeign() {
    return findChildByType(FOREIGN);
  }

  @Override
  @Nullable
  public PsiElement getKey() {
    return findChildByType(KEY);
  }

  @Override
  @Nullable
  public PsiElement getPrimary() {
    return findChildByType(PRIMARY);
  }

  @Override
  @Nullable
  public PsiElement getUnique() {
    return findChildByType(UNIQUE);
  }

}
