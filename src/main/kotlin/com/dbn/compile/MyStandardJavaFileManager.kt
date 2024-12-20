package com.dbn.compile

import javax.tools.FileObject
import javax.tools.ForwardingJavaFileManager
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject

/**
 * @author yudong
 */
class MyStandardJavaFileManager(fileManager: JavaFileManager, private val cl: MyClassLoader) :
    ForwardingJavaFileManager<JavaFileManager>(fileManager) {

    override fun getJavaFileForOutput(
        location: JavaFileManager.Location,
        className: String,
        kind: JavaFileObject.Kind,
        sibling: FileObject,
    ): JavaFileObject {
        val fileObject = MyJavaFileObject(className)
        cl.addCode(fileObject)
        return fileObject
    }

    override fun getClassLoader(location: JavaFileManager.Location): ClassLoader {
        return cl
    }
}
