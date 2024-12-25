package com.dbn.generate

import com.dbn.common.Pair
import com.dbn.utils.StringUtil
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import com.google.common.collect.Maps
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.StreamUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiImportList
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import lombok.SneakyThrows
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.*

class GenerateHelper(private val project: Project) {
    private var historyConfigObj: JsonObject

    init {
        val configPath = project.basePath + "/.idea/" + CONFIG_FILE_NAME
        val configFile = File(configPath)
        val virtualFile = VfsUtil.findFileByIoFile(configFile, true)

        historyConfigObj = if (virtualFile != null) {
            JsonParser.parseString(VfsUtil.loadText(virtualFile)) as JsonObject
        } else {
            JsonObject()
        }
    }

    val firstConfig: JsonObject?
        get() {
            val keySet = historyConfigObj.keySet()
            if (keySet.isEmpty()) {
                return null
            }

            val name = keySet.iterator().next()
            return getConfig(name)
        }

    fun getConfig(name: String?): JsonObject {
        return historyConfigObj.getAsJsonObject(name)
    }

    val configNames: Set<String>
        get() = historyConfigObj.keySet()

    fun validateParams(jsonObject: JsonObject): List<String?> {
        return jsonObject.entrySet().stream()
            .map { it: Map.Entry<String, JsonElement> ->
                val key = it.key
                val jsonElement = it.value
                val value = jsonElement.asString
                if (StringUtils.isNotBlank(value)) {
                    return@map null
                }

                key + "不能为空"
            }
            .filter { obj -> Objects.nonNull(obj) }
            .toList()
    }

    fun saveAndGenerate(jsonObject: JsonObject, saveFile: Boolean) {
        val scope = saveConfig(jsonObject)

        val businessName = jsonObject["businessNameTextField"].asString
        val businessNamePrefix = StringUtil.capitalizeFirstWord2(businessName)
        scope["businessNamePrefix"] = businessNamePrefix

        val methodName = jsonObject["methodNameTextField"].asString
        val modelName = StringUtil.capitalizeFirstWord2(methodName).replace("List", "")
        scope["modelNamePrefix"] = modelName

        val reqPair = savePojoFile(modelName, scope, "Req", jsonObject, saveFile)
        val resPair = savePojoFile(modelName, scope, "Res", jsonObject, saveFile)

        scope["modelPackage"] = reqPair.second()
        scope["reqFileName"] = reqPair.first()
        scope["resFileName"] = resPair.first()

        val interfacePair = saveInterface(businessNamePrefix, scope, false, jsonObject, saveFile)

        scope["serviceFileName"] = interfacePair.first()
        scope["serviceFieldName"] = StringUtil.toCamelCase(interfacePair.first())
        scope["servicePackage"] = interfacePair.second()

        scope["serviceImplPackage"] = interfacePair.second() + ".impl"
        saveInterface(businessNamePrefix, scope, true, jsonObject, saveFile)

        saveController(businessNamePrefix, scope, jsonObject, saveFile)
    }

    private fun getRootFolder(jsonObject: JsonObject): String {
        val rootFolder = jsonObject["projectFolderBtn"].asString
        val path = jsonObject["pathTextField"].asString
        return "$rootFolder/$path"
    }

    private fun obtainVirtualFile(): VirtualFile {
        val configPath = project.basePath + "/.idea/" + CONFIG_FILE_NAME
        val configFile = File(configPath)
        var virtualFile = VfsUtil.findFileByIoFile(configFile, true)
        if (virtualFile == null) {
            val parent = VfsUtil.findFileByIoFile(File(project.basePath + "/.idea"), false)
            val directory = PsiManager.getInstance(project).findDirectory(parent!!)
            val psiFile = directory!!.createFile(CONFIG_FILE_NAME)
            virtualFile = psiFile.virtualFile
        }

        return virtualFile!!
    }

    @SneakyThrows
    private fun saveConfig(jsonObject: JsonObject): MutableMap<String, String> {
        val virtualFile = obtainVirtualFile()

        val name = jsonObject["nameTextField"].asString

        historyConfigObj.add(name, jsonObject)

        val str = historyConfigObj.toString()

        VfsUtil.saveText(virtualFile, str)

        val scope: MutableMap<String, String> = Maps.newHashMap()
        for ((key, value) in jsonObject.entrySet()) {
            scope[key] = value.asString
        }

        return scope
    }

    @SneakyThrows
    fun removeConfig(name: String) {
        historyConfigObj.remove(name)

        val str = historyConfigObj.toString()

        val virtualFile = obtainVirtualFile()

        VfsUtil.saveText(virtualFile, str)
    }

    private fun savePojoFile(
        modelName: String,
        scope: Map<String, Any?>,
        name: String,
        jsonObject: JsonObject,
        saveFile: Boolean,
    ): Pair<String, String> {
        val mustache = readMustache("$name.mustache")

        val resStr = writeMustache(mustache, scope)

        val pojoPackage = jsonObject["pojoPackageTextField"].asString
        val pojoPostfix = jsonObject["pojoPostfixTextField"].asString
        val rootFolder = getRootFolder(jsonObject)

        val fileName = modelName + name + pojoPostfix
        if (!saveFile) {
            return Pair.of(fileName, pojoPackage)
        }

        saveFile(fileName, resStr, pojoPackage, rootFolder)

        return Pair.of(fileName, pojoPackage)
    }

    private fun saveInterface(
        businessNamePrefix: String,
        scope: Map<String, String>,
        isImpl: Boolean,
        jsonObject: JsonObject, saveFile: Boolean,
    ): Pair<String, String> {
        val servicePackage = jsonObject["servicePackageTextField"].asString
        val servicePostfix = jsonObject["servicePostfixTextField"].asString
        val rootFolder = getRootFolder(jsonObject)

        val fileName: String
        val fullPackage: String
        val mustache: Mustache
        if (isImpl) {
            fileName = businessNamePrefix + servicePostfix + "Impl"
            mustache = readMustache("InterfaceImpl.mustache")
            fullPackage = "$servicePackage.impl"
        } else {
            fileName = businessNamePrefix + servicePostfix
            mustache = readMustache("Interface.mustache")
            fullPackage = servicePackage
        }

        val resStr = writeMustache(mustache, scope)

        if (!saveFile) {
            return Pair.of(fileName, fullPackage)
        }

        if (fileNotExists(fileName, fullPackage)) {
            saveFile(fileName, resStr, fullPackage, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendFile(scope, "$fullPackage.$fileName", isImpl)
            }
        }

        return Pair.of(fileName, fullPackage)
    }

    private fun appendFile(jsonObject: Map<String, String>, rootClassName: String, isImpl: Boolean) {
        val modelPackage = jsonObject["modelPackage"]
        val reqFileName = jsonObject["reqFileName"]
        val resFileName = jsonObject["resFileName"]
        val methodNameTextField = jsonObject["methodNameTextField"]

        val searchScope = GlobalSearchScope.projectScope(project)
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val psiClass = javaPsiFacade.findClass(rootClassName, searchScope)!!

        val psiImportList = PsiTreeUtil.getChildOfType(psiClass.containingFile, PsiImportList::class.java)

        val elementFactory = javaPsiFacade.elementFactory
        val req = javaPsiFacade.findClass("$modelPackage.$reqFileName", searchScope)
        val reqImport = elementFactory.createImportStatement(req!!)
        psiImportList!!.add(reqImport)

        val res = javaPsiFacade.findClass("$modelPackage.$resFileName", searchScope)
        val resImport = elementFactory.createImportStatement(res!!)
        psiImportList.add(resImport)

        val methodContent = if (isImpl) {
            """
                public $resFileName $methodNameTextField($reqFileName reqVo) {
                    // TODO Auto-generated
                    return new $resFileName();                
                }
            """.trimIndent()
        } else {
            """
                /**
                 * TODO Auto-generate
                 */                
                $resFileName $methodNameTextField($reqFileName reqVo);
            """.trimIndent()
        }
        val method = elementFactory.createMethodFromText(methodContent, psiClass)
        psiClass.add(method)
    }

    private fun appendFile(jsonObject: Map<String, String>, rootClassName: String) {
        val modelPackage = jsonObject["modelPackage"]
        val reqFileName = jsonObject["reqFileName"]
        val resFileName = jsonObject["resFileName"]
        val methodNameTextField = jsonObject["methodNameTextField"]
        val serviceFieldName = jsonObject["serviceFieldName"]

        val searchScope = GlobalSearchScope.projectScope(project)
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val psiClass = javaPsiFacade.findClass(rootClassName, searchScope)!!

        val psiImportList = PsiTreeUtil.getChildOfType(psiClass.containingFile, PsiImportList::class.java)

        val elementFactory = javaPsiFacade.elementFactory
        val req = javaPsiFacade.findClass("$modelPackage.$reqFileName", searchScope)
        val reqImport = elementFactory.createImportStatement(req!!)
        psiImportList!!.add(reqImport)

        val res = javaPsiFacade.findClass("$modelPackage.$resFileName", searchScope)
        val resImport = elementFactory.createImportStatement(res!!)
        psiImportList.add(resImport)

        val methodContent =
            """
                @ApiOperation(value = "TODO Auto-generate")
                @PostMapping("/$methodNameTextField")
                public Result<$resFileName> $methodNameTextField(@Validated @RequestBody $reqFileName reqVo) {
                    return Result.success($serviceFieldName.$methodNameTextField(reqVo));
                }
            """.trimIndent()

        val method = elementFactory.createMethodFromText(methodContent, psiClass)
        psiClass.add(method)
    }

    private fun saveController(
        businessNamePrefix: String, scope: MutableMap<String, String>,
        jsonObject: JsonObject, saveFile: Boolean,
    ) {
        val searchScope = GlobalSearchScope.projectScope(project)
        val results = PsiShortNamesCache.getInstance(project).getClassesByName("Result", searchScope)
        if (results.isNotEmpty()) {
            val result = results[0]
            scope["resultPackage"] = PsiUtil.getPackageName(result) + ""
            scope["resultFileName"] = "Result"
        }

        val mustache = readMustache("Controller.mustache")

        val resStr = writeMustache(mustache, scope)

        val controllerPackage = jsonObject["controllerPackageTextField"].asString
        val controllerPostfix = jsonObject["controllerPostfixTextField"].asString
        val rootFolder = getRootFolder(jsonObject)

        val fileName = businessNamePrefix + controllerPostfix

        if (!saveFile) {
            return
        }

        if (fileNotExists(fileName, controllerPackage)) {
            saveFile(fileName, resStr, controllerPackage, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendFile(scope, "$controllerPackage.$fileName")
            }
        }
    }

    private fun readMustache(name: String): Mustache {
        val url = javaClass.classLoader.getResource("generate/$name")!!
        return url.openStream().use {
            val str = String(StreamUtil.readBytes(it), StandardCharsets.UTF_8)
            mf.compile(StringReader(str), name)
        }
    }

    private fun writeMustache(mustache: Mustache, scope: Map<String, Any?>): String {
        return StringWriter().use {
            mustache.execute(it, scope).flush()
            it.toString()
        }
    }

    private fun saveFile(fileName: String, content: String, packageName: String, rootFolder: String) {
        val tmpFileName = "$fileName.java"

        val psiDirectory: PsiDirectory
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val modelPackage = javaPsiFacade.findPackage(packageName)
        if (modelPackage == null) {
            val parent = VfsUtil.findFileByIoFile(File(rootFolder), false)
            val virtualFile = VfsUtil.createDirectoryIfMissing(parent, packageName.replace(".", "/"))
            psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile)!!
        } else {
            psiDirectory = modelPackage.directories[0]
        }

        var psiFile = psiDirectory.findFile(tmpFileName)
        if (psiFile == null) {
            psiFile = psiDirectory.createFile(tmpFileName)
        }

        val virtualFile = psiFile.virtualFile

        VfsUtil.saveText(virtualFile, content)
    }

    private fun fileNotExists(fileName: String, packageName: String): Boolean {
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val modelPackage = javaPsiFacade.findPackage(packageName) ?: return true

        val psiDirectory = modelPackage.directories[0]

        val psiFile = psiDirectory.findFile("$fileName.java")

        return psiFile == null
    }

    companion object {
        private const val CONFIG_FILE_NAME = "dbnavigator-generate-config.json"
        private val mf: MustacheFactory = DefaultMustacheFactory()
    }
}
