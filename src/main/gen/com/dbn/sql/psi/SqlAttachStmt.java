// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlAttachStmt extends PsiElement {

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlExpr getExpr();

  @Nullable
  PsiElement getAs();

  @NotNull
  PsiElement getAttach();

  @Nullable
  PsiElement getDatabase();

}
