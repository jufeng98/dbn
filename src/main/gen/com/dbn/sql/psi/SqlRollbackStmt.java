// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlRollbackStmt extends PsiElement {

  @Nullable
  SqlSavepointName getSavepointName();

  @NotNull
  PsiElement getRollback();

  @Nullable
  PsiElement getSavepoint();

  @Nullable
  PsiElement getTo();

  @Nullable
  PsiElement getTransaction();

}
