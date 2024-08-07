package com.dbn.navigation.psi

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.cache.CacheDbTable
import com.dbn.cache.MetadataCacheService
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project

@Suppress("unused")
class DbnToolWindowPsiElement(
    @Suppress("MemberVisibilityCanBePrivate") val tableNames: Set<String>,
    val columnName: String?,
    node: ASTNode,
) :
    ASTWrapperPsiElement(node) {

    override fun navigate(requestFocus: Boolean) {
        val browserManager = DatabaseBrowserManager.getInstance(project)
        browserManager.navigateToElement(project, tableNames, columnName)
    }

    companion object {
        @Suppress("unused")
        fun getFirstConnCacheDbTables(project: Project): Map<String, CacheDbTable>? {
            val cacheService = MetadataCacheService.getService(project)
            return cacheService.getFirstConnectionDBCacheTables(project)
        }
    }
}