// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlBinaryLikeExpr extends SqlExpr {

  @NotNull
  SqlBinaryLikeOperator getBinaryLikeOperator();

  @NotNull
  List<SqlExpr> getExprList();

  @Nullable
  PsiElement getEscape();

  @Nullable
  PsiElement getNot();

}
