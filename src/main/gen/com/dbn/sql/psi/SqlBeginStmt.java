// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlBeginStmt extends PsiElement {

  @NotNull
  PsiElement getBegin();

  @Nullable
  PsiElement getDeferred();

  @Nullable
  PsiElement getExclusive();

  @Nullable
  PsiElement getImmediate();

  @Nullable
  PsiElement getTransaction();

}
