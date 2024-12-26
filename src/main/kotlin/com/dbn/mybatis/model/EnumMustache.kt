package com.dbn.mybatis.model

/**
 * @author yudong
 */
class EnumMustache {
    lateinit var enumPath: String
    lateinit var enumClassName: String
    var useLombokPlugin: Boolean = false
    lateinit var enumFields: List<EnumMustacheField>
}
