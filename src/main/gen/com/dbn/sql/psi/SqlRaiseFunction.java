// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlRaiseFunction extends PsiElement {

  @Nullable
  SqlErrorMessage getErrorMessage();

  @Nullable
  PsiElement getAbort();

  @Nullable
  PsiElement getFail();

  @Nullable
  PsiElement getIgnore();

  @NotNull
  PsiElement getRaise();

  @Nullable
  PsiElement getRollback();

}
