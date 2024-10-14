// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlCreateTableStmt extends PsiElement {

  @NotNull
  List<SqlColumnDef> getColumnDefList();

  @Nullable
  SqlCompoundSelectStmt getCompoundSelectStmt();

  @Nullable
  SqlDatabaseName getDatabaseName();

  @NotNull
  List<SqlTableAttr> getTableAttrList();

  @NotNull
  List<SqlTableConstraint> getTableConstraintList();

  @NotNull
  SqlTableName getTableName();

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

  @NotNull
  PsiElement getTable();

  @Nullable
  PsiElement getTemp();

  @Nullable
  PsiElement getTemporary();

  @Nullable
  PsiElement getWithout();

}
