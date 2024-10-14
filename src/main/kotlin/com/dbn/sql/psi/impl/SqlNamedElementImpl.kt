package com.dbn.sql.psi.impl

import com.dbn.sql.psi.SqlNamedElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

/**
 * @author yudong
 */
abstract class SqlNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), SqlNamedElement
