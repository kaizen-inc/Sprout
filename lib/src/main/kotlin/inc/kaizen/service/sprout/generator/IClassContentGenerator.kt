package inc.kaizen.service.sprout.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.toCamelCase

val BASE_PACKAGE_NAME = "basePackageName"
val PACKAGE_NAME = "packageName"
val SERVICE_NAME = "serviceName"
val CLASS_NAME = "className"
val FILE_PATH = "filePath"
val MODEL_PACKAGE_NAME = "modelPackageName"

interface IClassContentGenerator {

    fun generate(codeGenerator: CodeGenerator, extensions: Map<String, Any>) {
        val tempExtensions = extensions.toMutableMap()
        val basePackageName = extensions[BASE_PACKAGE_NAME] as String
        val serviceName = extensions[SERVICE_NAME] as String

        val className = className(serviceName)
        val packageName = packageName(basePackageName, serviceName)
        val filePath = "${packageName.replace('.', '/')}/$className"
        if(!codeGenerator.generatedFile.map { it.nameWithoutExtension }.contains(className)) { //FIXME this is a hack to avoid generating the same file multiple times
            val file = codeGenerator.createNewFileByPath(
                Dependencies(false),
                filePath
            )

            tempExtensions[CLASS_NAME] = className
            tempExtensions[PACKAGE_NAME] = packageName
            tempExtensions[FILE_PATH] = filePath

            val content = generateContent(tempExtensions).toByteArray()
            file.write(content)
            file.close()
        }
    }

    fun generateContent(extensions: Map<String, Any>): String

    fun classNameSuffix(): String

    fun packageNameSuffix(): String = classNameSuffix().toCamelCase()

    fun className(serviceName: String) = "${serviceName.toCamelCase().capitalizeFirstLetter()}${classNameSuffix()}"

    fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.${serviceName.toCamelCase()}.${packageNameSuffix().toCamelCase()}"
}