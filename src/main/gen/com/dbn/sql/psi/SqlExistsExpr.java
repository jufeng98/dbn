// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlExistsExpr extends SqlExpr {

  @NotNull
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  PsiElement getExists();

  @Nullable
  PsiElement getNot();

}
