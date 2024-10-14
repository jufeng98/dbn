package com.dbn.cache

data class CacheDbColumn(
    val name: String,
    val columnComment: String,
    val columnDefault: String,
    val position: Short,
    val isNullable: Boolean,
    val isHidden: Boolean,
    val isPrimaryKey: Boolean,
    val isForeignKey: Boolean,
    val isUniqueKey: Boolean,
    val isIdentity: Boolean,
) {
    lateinit var cacheDbDataType: CacheDbDataType
}
