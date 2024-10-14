package com.dbn.cache

data class CacheDbIndex(
    val name: String,
    val isUnique: Boolean,
    val isValid: Boolean,
    var columnNames: Collection<String>,
)
