// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlBetweenExpr extends SqlExpr {

  @NotNull
  List<SqlExpr> getExprList();

  @Nullable
  PsiElement getAnd();

  @NotNull
  PsiElement getBetween();

  @Nullable
  PsiElement getNot();

}
