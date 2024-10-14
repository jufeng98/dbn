// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.dbn.sql.psi.SqlTypes.*;
import com.dbn.sql.SqlPsiElement;
import com.dbn.sql.psi.*;

public class SqlPragmaStmtImpl extends SqlPsiElement implements SqlPragmaStmt {

  public SqlPragmaStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SqlVisitor visitor) {
    visitor.visitPragmaStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SqlVisitor) accept((SqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SqlDatabaseName getDatabaseName() {
    return findChildByClass(SqlDatabaseName.class);
  }

  @Override
  @Nullable
  public SqlPragmaName getPragmaName() {
    return findChildByClass(SqlPragmaName.class);
  }

  @Override
  @Nullable
  public SqlPragmaValue getPragmaValue() {
    return findChildByClass(SqlPragmaValue.class);
  }

  @Override
  @NotNull
  public PsiElement getPragma() {
    return findNotNullChildByType(PRAGMA);
  }

}
