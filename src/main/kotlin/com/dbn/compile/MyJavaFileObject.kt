package com.dbn.compile

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.URI
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject

/**
 * @author yudong
 */
class MyJavaFileObject(val className: String) : SimpleJavaFileObject(URI(className), JavaFileObject.Kind.CLASS) {
    private val baos = ByteArrayOutputStream()

    override fun openOutputStream(): OutputStream {
        return baos
    }

    val byteCode: ByteArray
        get() = baos.toByteArray()
}
