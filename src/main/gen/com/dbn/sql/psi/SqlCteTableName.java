// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlCteTableName extends PsiElement {

  @NotNull
  List<SqlColumnAlias> getColumnAliasList();

  @NotNull
  SqlTableName getTableName();

}
