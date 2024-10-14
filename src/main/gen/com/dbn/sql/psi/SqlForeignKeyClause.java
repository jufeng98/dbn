// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlForeignKeyClause extends PsiElement {

  @NotNull
  List<SqlColumnName> getColumnNameList();

  @NotNull
  SqlForeignTable getForeignTable();

  @NotNull
  List<SqlIdentifier> getIdentifierList();

  @Nullable
  PsiElement getDeferred();

  @Nullable
  PsiElement getImmediate();

  @Nullable
  PsiElement getNot();

  @NotNull
  PsiElement getReferencesWord();

}
