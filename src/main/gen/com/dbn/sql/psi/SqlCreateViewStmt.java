// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlCreateViewStmt extends PsiElement {

  @NotNull
  List<SqlColumnAlias> getColumnAliasList();

  @Nullable
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @NotNull
  SqlViewName getViewName();

  @Nullable
  PsiElement getAs();

  @NotNull
  PsiElement getCreate();

  @Nullable
  PsiElement getExists();

  @Nullable
  PsiElement getIf();

  @Nullable
  PsiElement getNot();

  @Nullable
  PsiElement getTemp();

  @Nullable
  PsiElement getTemporary();

  @NotNull
  PsiElement getView();

}
