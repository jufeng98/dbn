// This is a generated file. Not intended for manual editing.
package com.dbn.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlLiteralValue extends PsiElement {

  @Nullable
  SqlBlobLiteral getBlobLiteral();

  @Nullable
  SqlNumericLiteral getNumericLiteral();

  @Nullable
  SqlStringLiteral getStringLiteral();

  @Nullable
  PsiElement getCurrentDate();

  @Nullable
  PsiElement getCurrentTime();

  @Nullable
  PsiElement getCurrentTimestamp();

  @Nullable
  PsiElement getNull();

}
