// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlReindexStmt extends PsiElement {

  @Nullable
  SqlCollationName getCollationName();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlIndexName getIndexName();

  @Nullable
  SqlTableName getTableName();

  @NotNull
  PsiElement getReindex();

}
