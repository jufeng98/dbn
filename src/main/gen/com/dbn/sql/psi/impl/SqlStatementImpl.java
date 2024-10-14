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
import com.intellij.psi.PsiReference;

public class SqlStatementImpl extends SqlPsiElement implements SqlStatement {

  public SqlStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlAlterTableStmt getAlterTableStmt() {
    return findChildByClass(SqlAlterTableStmt.class);
  }

  @Override
  @Nullable
  public SqlAnalyzeStmt getAnalyzeStmt() {
    return findChildByClass(SqlAnalyzeStmt.class);
  }

  @Override
  @Nullable
  public SqlAttachStmt getAttachStmt() {
    return findChildByClass(SqlAttachStmt.class);
  }

  @Override
  @Nullable
  public SqlBeginStmt getBeginStmt() {
    return findChildByClass(SqlBeginStmt.class);
  }

  @Override
  @Nullable
  public SqlCommitStmt getCommitStmt() {
    return findChildByClass(SqlCommitStmt.class);
  }

  @Override
  @Nullable
  public SqlCompoundSelectStmt getCompoundSelectStmt() {
    return findChildByClass(SqlCompoundSelectStmt.class);
  }

  @Override
  @Nullable
  public SqlCreateIndexStmt getCreateIndexStmt() {
    return findChildByClass(SqlCreateIndexStmt.class);
  }

  @Override
  @Nullable
  public SqlCreateTableStmt getCreateTableStmt() {
    return findChildByClass(SqlCreateTableStmt.class);
  }

  @Override
  @Nullable
  public SqlCreateTriggerStmt getCreateTriggerStmt() {
    return findChildByClass(SqlCreateTriggerStmt.class);
  }

  @Override
  @Nullable
  public SqlCreateViewStmt getCreateViewStmt() {
    return findChildByClass(SqlCreateViewStmt.class);
  }

  @Override
  @Nullable
  public SqlCreateVirtualTableStmt getCreateVirtualTableStmt() {
    return findChildByClass(SqlCreateVirtualTableStmt.class);
  }

  @Override
  @Nullable
  public SqlDeleteStmtLimited getDeleteStmtLimited() {
    return findChildByClass(SqlDeleteStmtLimited.class);
  }

  @Override
  @Nullable
  public SqlDetachStmt getDetachStmt() {
    return findChildByClass(SqlDetachStmt.class);
  }

  @Override
  @Nullable
  public SqlDropIndexStmt getDropIndexStmt() {
    return findChildByClass(SqlDropIndexStmt.class);
  }

  @Override
  @Nullable
  public SqlDropTableStmt getDropTableStmt() {
    return findChildByClass(SqlDropTableStmt.class);
  }

  @Override
  @Nullable
  public SqlDropTriggerStmt getDropTriggerStmt() {
    return findChildByClass(SqlDropTriggerStmt.class);
  }

  @Override
  @Nullable
  public SqlDropViewStmt getDropViewStmt() {
    return findChildByClass(SqlDropViewStmt.class);
  }

  @Override
  @Nullable
  public SqlInsertStmt getInsertStmt() {
    return findChildByClass(SqlInsertStmt.class);
  }

  @Override
  @Nullable
  public SqlPragmaStmt getPragmaStmt() {
    return findChildByClass(SqlPragmaStmt.class);
  }

  @Override
  @Nullable
  public SqlReindexStmt getReindexStmt() {
    return findChildByClass(SqlReindexStmt.class);
  }

  @Override
  @Nullable
  public SqlReleaseStmt getReleaseStmt() {
    return findChildByClass(SqlReleaseStmt.class);
  }

  @Override
  @Nullable
  public SqlRollbackStmt getRollbackStmt() {
    return findChildByClass(SqlRollbackStmt.class);
  }

  @Override
  @Nullable
  public SqlSavepointStmt getSavepointStmt() {
    return findChildByClass(SqlSavepointStmt.class);
  }

  @Override
  @Nullable
  public SqlUpdateStmtLimited getUpdateStmtLimited() {
    return findChildByClass(SqlUpdateStmtLimited.class);
  }

  @Override
  @Nullable
  public SqlVacuumStmt getVacuumStmt() {
    return findChildByClass(SqlVacuumStmt.class);
  }

  @Override
  @Nullable
  public PsiElement getExplain() {
    return findChildByType(EXPLAIN);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return SqlPsiImplUtil.getReferences(this);
  }

}
