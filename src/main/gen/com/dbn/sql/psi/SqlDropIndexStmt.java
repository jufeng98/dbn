// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlDropIndexStmt extends PsiElement {

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlIndexName getIndexName();

  @NotNull
  PsiElement getDrop();

  @Nullable
  PsiElement getExists();

  @Nullable
  PsiElement getIf();

  @NotNull
  PsiElement getIndex();

}