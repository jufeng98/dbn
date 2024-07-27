package com.dbn.cache

data class CacheDbIndex(
    val indexName: String,
    val isUnique: Boolean,
    val isValid: Boolean,
)
