// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlDeleteStmt extends PsiElement {

  @Nullable
  SqlExpr getExpr();

  @NotNull
  SqlQualifiedTableName getQualifiedTableName();

  @Nullable
  SqlWithClause getWithClause();

  @NotNull
  PsiElement getDelete();

  @NotNull
  PsiElement getFrom();

  @Nullable
  PsiElement getWhere();

}
