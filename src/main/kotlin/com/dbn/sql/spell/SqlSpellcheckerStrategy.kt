package com.dbn.sql.spell

import com.dbn.sql.psi.SqlStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer

/**
 * @author yudong
 */
class SqlSpellcheckerStrategy : SpellcheckingStrategy() {
    private val ourStringLiteralTokenizer: Tokenizer<SqlStringLiteral> = object : Tokenizer<SqlStringLiteral>() {
        override fun tokenize(element: SqlStringLiteral, consumer: TokenConsumer) {
            val textSplitter = PlainTextSplitter.getInstance()
            consumer.consumeToken(element, textSplitter)
        }
    }

    override fun getTokenizer(element: PsiElement?): Tokenizer<*> {
        if (element is SqlStringLiteral) {
            return ourStringLiteralTokenizer
        }

        return super.getTokenizer(element)
    }

}
