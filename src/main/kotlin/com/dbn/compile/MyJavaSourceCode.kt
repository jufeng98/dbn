package com.dbn.compile

import java.net.URI
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject

/**
 * @author yudong
 */
class MyJavaSourceCode(val className: String, private var code: String) :
    SimpleJavaFileObject(
        URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
        JavaFileObject.Kind.SOURCE
    ) {

    override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence {
        return code
    }
}
