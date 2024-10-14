// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlJoinConstraint extends PsiElement {

  @NotNull
  List<SqlColumnName> getColumnNameList();

  @Nullable
  SqlExpr getExpr();

  @Nullable
  PsiElement getOn();

  @Nullable
  PsiElement getUsing();

}
