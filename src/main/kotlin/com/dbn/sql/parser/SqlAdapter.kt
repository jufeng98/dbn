package com.dbn.sql.parser

import com.dbn.sql._SqlLexer
import com.intellij.lexer.FlexAdapter

/**
 * @author yudong
 */
class SqlAdapter : FlexAdapter(_SqlLexer(null))
