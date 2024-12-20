package com.dbn.compile

import com.dbn.DatabaseNavigator
import java.io.File
import java.net.URLClassLoader
import javax.tools.DiagnosticCollector
import javax.tools.JavaCompiler
import javax.tools.JavaFileObject
import javax.tools.ToolProvider
import kotlin.io.path.absolutePathString

/**
 * @author yudong
 */
object MyJavaCompiler {

    fun compile(className: String, sourceCode: String, classLoader: MyClassLoader, vararg options: String) {
        val myJavaSourceCode = MyJavaSourceCode(className, sourceCode)

        val compilationUnits: Collection<MyJavaSourceCode> = listOf(myJavaSourceCode)

        val javac = systemJavaCompiler

        val diagnostic = DiagnosticCollector<JavaFileObject>()

        val standardFileManager = javac.getStandardFileManager(null, null, null)
        val fileManager = MyStandardJavaFileManager(standardFileManager, classLoader)

        val task = javac.getTask(null, fileManager, diagnostic, options.toList(), null, compilationUnits)
        val success = task.call()

        if (!success) {
            val message = diagnostic.diagnostics.joinToString()
            throw RuntimeException(className + "编译失败,原因:$message")
        }
    }

    @JvmStatic
    val systemJavaCompiler: JavaCompiler
        get() {
            val systemJavaCompiler = ToolProvider.getSystemJavaCompiler()
            if (systemJavaCompiler != null) {
                return systemJavaCompiler
            }

            val file = loadJavacJar()

            val urlClassLoader = URLClassLoader(arrayOf(file.toURI().toURL()))

            val javaCompilerClass = urlClassLoader.loadClass("javax.tools.JavaCompiler")
            val javacToolClass = Class.forName("com.sun.tools.javac.api.JavacTool", true, urlClassLoader)
            val subclass = javacToolClass.asSubclass(javaCompilerClass)

            return subclass.getConstructor().newInstance() as JavaCompiler
        }

    private fun loadJavacJar(): File {
        val libraryRoot = "/ext/mybatis"
        val pluginDescriptor = DatabaseNavigator.getPluginDescriptor()
        val pluginPath = pluginDescriptor.pluginPath
        return File(pluginPath.absolutePathString() + libraryRoot, "javac.jar")
    }

}
