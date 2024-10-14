// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlWithClause extends PsiElement {

  @NotNull
  List<SqlCompoundSelectStmt> getCompoundSelectStmtList();

  @NotNull
  List<SqlCteTableName> getCteTableNameList();

  @Nullable
  PsiElement getRecursive();

  @NotNull
  PsiElement getWith();

}
