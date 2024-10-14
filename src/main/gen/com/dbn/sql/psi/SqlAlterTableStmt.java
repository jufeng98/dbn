// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlAlterTableStmt extends PsiElement {

  @Nullable
  SqlColumnDef getColumnDef();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlNewTableName getNewTableName();

  @Nullable
  SqlTableName getTableName();

  @Nullable
  PsiElement getAdd();

  @NotNull
  PsiElement getAlter();

  @Nullable
  PsiElement getColumn();

  @Nullable
  PsiElement getRename();

  @Nullable
  PsiElement getTable();

  @Nullable
  PsiElement getTo();

}
