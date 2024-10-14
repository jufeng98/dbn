// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface SqlStatement extends PsiElement {

  @Nullable
  SqlAlterTableStmt getAlterTableStmt();

  @Nullable
  SqlAnalyzeStmt getAnalyzeStmt();

  @Nullable
  SqlAttachStmt getAttachStmt();

  @Nullable
  SqlBeginStmt getBeginStmt();

  @Nullable
  SqlCommitStmt getCommitStmt();

  @Nullable
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  SqlCreateIndexStmt getCreateIndexStmt();

  @Nullable
  SqlCreateTableStmt getCreateTableStmt();

  @Nullable
  SqlCreateTriggerStmt getCreateTriggerStmt();

  @Nullable
  SqlCreateViewStmt getCreateViewStmt();

  @Nullable
  SqlCreateVirtualTableStmt getCreateVirtualTableStmt();

  @Nullable
  SqlDeleteStmtLimited getDeleteStmtLimited();

  @Nullable
  SqlDetachStmt getDetachStmt();

  @Nullable
  SqlDropIndexStmt getDropIndexStmt();

  @Nullable
  SqlDropTableStmt getDropTableStmt();

  @Nullable
  SqlDropTriggerStmt getDropTriggerStmt();

  @Nullable
  SqlDropViewStmt getDropViewStmt();

  @Nullable
  SqlInsertStmt getInsertStmt();

  @Nullable
  SqlPragmaStmt getPragmaStmt();

  @Nullable
  SqlReindexStmt getReindexStmt();

  @Nullable
  SqlReleaseStmt getReleaseStmt();

  @Nullable
  SqlRollbackStmt getRollbackStmt();

  @Nullable
  SqlSavepointStmt getSavepointStmt();

  @Nullable
  SqlUpdateStmtLimited getUpdateStmtLimited();

  @Nullable
  SqlVacuumStmt getVacuumStmt();

  @Nullable
  PsiElement getExplain();

  @NotNull
  PsiReference[] getReferences();

}
