package com.dbn.compile

import java.net.URL
import java.net.URLClassLoader

/**
 * @author yudong
 */
class MyClassLoader(urls: Array<URL>, parent: ClassLoader) : URLClassLoader(urls, parent) {
    private val fileObjectMutableMap: MutableMap<String, MyJavaFileObject> = mutableMapOf()

    fun addCode(cc: MyJavaFileObject) {
        fileObjectMutableMap[cc.name] = cc
    }

    override fun findClass(name: String): Class<*> {
        val cc = fileObjectMutableMap[name] ?: return super.findClass(name)

        val byteCode = cc.byteCode
        return defineClass(name, byteCode, 0, byteCode.size)
    }
}
