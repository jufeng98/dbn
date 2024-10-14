// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlIndexedColumn extends PsiElement {

  @Nullable
  SqlCollationName getCollationName();

  @NotNull
  SqlColumnName getColumnName();

  @Nullable
  PsiElement getAsc();

  @Nullable
  PsiElement getCollate();

  @Nullable
  PsiElement getDesc();

}
