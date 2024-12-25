package com.dbn.generate

import com.dbn.common.Pair
import com.dbn.utils.StringUtil
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
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
        saveConfig(jsonObject)

        val feign = jsonObject["feignRadioButton"].asBoolean
        val dubbo = jsonObject["dubboRadioButton"].asBoolean

        val scope: MutableMap<String, Any> = mutableMapOf()
        scope["feign"] = feign

        val businessName = jsonObject["businessNameTextField"].asString
        scope["businessName"] = businessName

        val businessNamePrefix = StringUtil.capitalizeFirstWord2(businessName)
        scope["businessNamePrefix"] = businessNamePrefix

        val methodName = jsonObject["methodNameTextField"].asString
        scope["methodName"] = methodName

        val methodNamePrefix = StringUtil.capitalizeFirstWord2(methodName).replace("List", "")

        if (dubbo) {
            saveDubboPojoFile(methodNamePrefix, scope, "Req", jsonObject)
            saveDubboPojoFile(methodNamePrefix, scope, "Res", jsonObject)

            saveDubboInterface(businessNamePrefix, scope, jsonObject, saveFile)
        } else {
            if (feign) {
                val rootFolder = getApiRootFolder(jsonObject)
                savePojoFile(methodNamePrefix, scope, "Req", jsonObject, rootFolder)
                savePojoFile(methodNamePrefix, scope, "Res", jsonObject, rootFolder)

                saveFeignInterface(businessNamePrefix, scope, jsonObject)
            } else {
                val rootFolder = getRootFolder(jsonObject)
                savePojoFile(methodNamePrefix, scope, "Req", jsonObject, rootFolder)
                savePojoFile(methodNamePrefix, scope, "Res", jsonObject, rootFolder)
            }

            saveInterfaceOrImpl(businessNamePrefix, scope, false, jsonObject)

            saveInterfaceOrImpl(businessNamePrefix, scope, true, jsonObject)

            saveController(businessNamePrefix, scope, jsonObject)
        }
    }

    private fun getRootFolder(jsonObject: JsonObject): String {
        val rootFolder = jsonObject["projectFolderBtn"].asString
        val path = jsonObject["pathTextField"].asString
        return "$rootFolder/$path"
    }

    private fun getApiRootFolder(jsonObject: JsonObject): String {
        val rootFolder = jsonObject["apiFolderBtn"].asString
        val path = jsonObject["apiPathTextField"].asString
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
    private fun saveConfig(jsonObject: JsonObject) {
        val virtualFile = obtainVirtualFile()

        val name = jsonObject["nameTextField"].asString

        historyConfigObj.add(name, jsonObject)

        val str = historyConfigObj.toString()

        VfsUtil.saveText(virtualFile, str)
    }

    @SneakyThrows
    fun removeConfig(name: String) {
        historyConfigObj.remove(name)

        val str = historyConfigObj.toString()

        val virtualFile = obtainVirtualFile()

        VfsUtil.saveText(virtualFile, str)
    }

    private fun savePojoFile(
        methodNamePrefix: String,
        scope: MutableMap<String, Any>,
        type: String,
        jsonObject: JsonObject,
        rootFolder: String,
    ) {
        val pojoPackage = jsonObject["pojoPackageTextField"].asString
        scope["pojoPackage"] = pojoPackage

        val pojoPostfix = jsonObject["pojoPostfixTextField"].asString
        val fileName = methodNamePrefix + type + pojoPostfix
        scope["pojo${type}FileName"] = fileName

        val mustache = readMustache("$type.mustache")
        val resStr = writeMustache(mustache, scope)

        saveFile(fileName, resStr, pojoPackage, rootFolder)
    }

    private fun saveDubboPojoFile(
        modelNamePrefix: String,
        scope: MutableMap<String, Any>,
        type: String,
        jsonObject: JsonObject,
    ) {
        val apiPackageTextField = jsonObject["apiPackageTextField"].asString
        scope["pojoPackage"] = apiPackageTextField

        val fileName = modelNamePrefix + type + "Dto"
        scope["pojo${type}FileName"] = fileName

        val mustache = readMustache("${type}Dubbo.mustache")
        val resStr = writeMustache(mustache, scope)

        val rootFolder = getApiRootFolder(jsonObject)
        saveFile(fileName, resStr, apiPackageTextField, rootFolder)
    }

    private fun saveInterfaceOrImpl(
        businessNamePrefix: String,
        scope: MutableMap<String, Any>,
        isImpl: Boolean,
        jsonObject: JsonObject,
    ) {
        val servicePackage = jsonObject["servicePackageTextField"].asString
        val servicePostfix = jsonObject["servicePostfixTextField"].asString

        val fileName: String
        val fullPackage: String
        val mustache: Mustache
        if (isImpl) {
            fileName = businessNamePrefix + servicePostfix + "Impl"
            scope["serviceImplFileName"] = fileName

            mustache = readMustache("InterfaceImpl.mustache")

            fullPackage = "$servicePackage.impl"
            scope["serviceImplPackage"] = fullPackage
        } else {
            fileName = businessNamePrefix + servicePostfix
            scope["serviceFileName"] = fileName
            scope["serviceFileFieldName"] = StringUtil.toCamelCase(fileName)

            mustache = readMustache("Interface.mustache")

            fullPackage = servicePackage
            scope["servicePackage"] = fullPackage
        }


        val resStr = writeMustache(mustache, scope)

        val rootFolder = getRootFolder(jsonObject)
        if (fileNotExists(fileName, fullPackage)) {
            saveFile(fileName, resStr, fullPackage, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendInterfaceOrImplFile(scope, "$fullPackage.$fileName", isImpl)
            }
        }
    }

    private fun appendInterfaceOrImplFile(scope: Map<String, Any>, rootClassName: String, isImpl: Boolean) {
        val pojoPackage = scope["pojoPackage"]
        val pojoReqFileName = scope["pojoReqFileName"]
        val pojoResFileName = scope["pojoResFileName"]
        val methodName = scope["methodName"]

        val searchScope = GlobalSearchScope.projectScope(project)
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val psiClass = javaPsiFacade.findClass(rootClassName, searchScope)!!

        val psiImportList = PsiTreeUtil.getChildOfType(psiClass.containingFile, PsiImportList::class.java)

        val elementFactory = javaPsiFacade.elementFactory
        val req = javaPsiFacade.findClass("$pojoPackage.$pojoReqFileName", searchScope)
        val reqImport = elementFactory.createImportStatement(req!!)
        psiImportList!!.add(reqImport)

        val res = javaPsiFacade.findClass("$pojoPackage.$pojoResFileName", searchScope)
        val resImport = elementFactory.createImportStatement(res!!)
        psiImportList.add(resImport)

        val methodContent = if (isImpl) {
            """
                public $pojoResFileName $methodName($pojoReqFileName reqVo) {
                    // TODO Auto-generated
                    return new $pojoResFileName();                
                }
            """.trimIndent()
        } else {
            """
                /**
                 * TODO Auto-generate
                 */                
                $pojoResFileName $methodName($pojoReqFileName reqVo);
            """.trimIndent()
        }
        val method = elementFactory.createMethodFromText(methodContent, psiClass)
        psiClass.add(method)
    }

    private fun appendControllerFile(scope: Map<String, Any>, rootClassName: String) {
        val pojoPackage = scope["pojoPackage"]
        val pojoReqFileName = scope["pojoReqFileName"]
        val pojoResFileName = scope["pojoResFileName"]
        val methodName = scope["methodNameTextField"]
        val serviceFieldName = scope["serviceFieldName"]

        val searchScope = GlobalSearchScope.projectScope(project)
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val psiClass = javaPsiFacade.findClass(rootClassName, searchScope)!!

        val psiImportList = PsiTreeUtil.getChildOfType(psiClass.containingFile, PsiImportList::class.java)

        val elementFactory = javaPsiFacade.elementFactory
        val req = javaPsiFacade.findClass("$pojoPackage.$pojoReqFileName", searchScope)
        val reqImport = elementFactory.createImportStatement(req!!)
        psiImportList!!.add(reqImport)

        val res = javaPsiFacade.findClass("$pojoPackage.$pojoResFileName", searchScope)
        val resImport = elementFactory.createImportStatement(res!!)
        psiImportList.add(resImport)

        val methodContent =
            """
                @ApiOperation(value = "TODO Auto-generate")
                @PostMapping("/$methodName")
                public Result<$pojoResFileName> $methodName(@Validated @RequestBody $pojoReqFileName reqVo) {
                    return Result.success($serviceFieldName.$methodName(reqVo));
                }
            """.trimIndent()

        val method = elementFactory.createMethodFromText(methodContent, psiClass)
        psiClass.add(method)
    }

    private fun saveDubboInterface(
        businessNamePrefix: String, scope: MutableMap<String, Any>,
        jsonObject: JsonObject, saveFile: Boolean,
    ): Pair<String, String> {
        val mustache = readMustache("Dubbo.mustache")

        val resStr = writeMustache(mustache, scope)

        val apiPackageTextField = jsonObject["apiPackageTextField"].asString
        val servicePostfixTextField = jsonObject["servicePostfixTextField"].asString

        val fileName = businessNamePrefix + "Dubbo" + servicePostfixTextField
        scope["serviceDubboFileName"] = fileName

        if (!saveFile) {
            return Pair.of(fileName, apiPackageTextField)
        }

        val rootFolder = getApiRootFolder(jsonObject)
        if (fileNotExists(fileName, apiPackageTextField)) {
            saveFile(fileName, resStr, apiPackageTextField, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendInterfaceOrImplFile(scope, "$apiPackageTextField.$fileName", false)
            }
        }

        return Pair.of(fileName, apiPackageTextField)
    }

    private fun saveFeignInterface(
        businessNamePrefix: String,
        scope: MutableMap<String, Any>,
        jsonObject: JsonObject,
    ) {
        val projectFolder = jsonObject["projectFolderBtn"].asString
        val idx = projectFolder.lastIndexOf("/")
        val moduleName = projectFolder.substring(idx + 1)
        scope["moduleName"] = moduleName

        val servicePostfixTextField = jsonObject["servicePostfixTextField"].asString

        val apiPackageTextField = jsonObject["apiPackageTextField"].asString
        scope["feignPackage"] = apiPackageTextField

        val feignFileName = businessNamePrefix + "Feign" + servicePostfixTextField
        scope["feignFileName"] = feignFileName

        val mustache = readMustache("Feign.mustache")
        val resStr = writeMustache(mustache, scope)

        val rootFolder = getApiRootFolder(jsonObject)
        if (fileNotExists(feignFileName, apiPackageTextField)) {
            saveFile(feignFileName, resStr, apiPackageTextField, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendInterfaceOrImplFile(scope, "$apiPackageTextField.$feignFileName", false)
            }
        }
    }

    private fun saveController(
        businessNamePrefix: String,
        scope: MutableMap<String, Any>,
        jsonObject: JsonObject,
    ) {
        val searchScope = GlobalSearchScope.projectScope(project)
        val results = PsiShortNamesCache.getInstance(project).getClassesByName("Result", searchScope)
        if (results.isNotEmpty()) {
            val result = results[0]
            scope["resultPackage"] = PsiUtil.getPackageName(result) + ""
            scope["resultFileName"] = "Result"
        }

        val controllerPackage = jsonObject["controllerPackageTextField"].asString
        scope["controllerPackage"] = controllerPackage

        val controllerPostfix = jsonObject["controllerPostfixTextField"].asString

        val feign = jsonObject["feignRadioButton"].asBoolean
        val fileName = if (feign) {
            businessNamePrefix + "Feign" + controllerPostfix
        } else {
            businessNamePrefix + controllerPostfix
        }
        scope["controllerFileName"] = fileName

        val mustache = readMustache("Controller.mustache")
        val resStr = writeMustache(mustache, scope)

        val rootFolder = getRootFolder(jsonObject)
        if (fileNotExists(fileName, controllerPackage)) {
            saveFile(fileName, resStr, controllerPackage, rootFolder)
        } else {
            WriteCommandAction.runWriteCommandAction(project) {
                appendControllerFile(scope, "$controllerPackage.$fileName")
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
