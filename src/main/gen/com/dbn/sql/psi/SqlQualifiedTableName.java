// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlQualifiedTableName extends PsiElement {

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlIndexName getIndexName();

  @NotNull
  SqlTableName getTableName();

  @Nullable
  PsiElement getBy();

  @Nullable
  PsiElement getIndexed();

  @Nullable
  PsiElement getNot();

}
