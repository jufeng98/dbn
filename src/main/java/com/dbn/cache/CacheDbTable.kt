package com.dbn.cache

import com.google.common.collect.Maps

/**
 * 由于DbTable等相关的类数据层和视图层紧紧耦合在一起,没法做到将数据层的东西单独抽出来并从本地缓存中初始化,因此新增CacheDbTable等相关类来实现,
 * 为sql的表名和列名的智能提示等功能提供基础
 */
data class CacheDbTable(val name: String, val comment: String, val isTemporary: Boolean) {
    @Suppress("MemberVisibilityCanBePrivate")
    val cacheDbColumnMap: MutableMap<String, CacheDbColumn> = Maps.newLinkedHashMap()

    fun addCacheDbColumn(cacheDbColumn: CacheDbColumn) {
        cacheDbColumnMap[cacheDbColumn.name] = cacheDbColumn
    }
}
