package inc.kaizen.service.sprout.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.toCamelCase

interface IClassContentGenerator {

    fun generate(codeGenerator: CodeGenerator, basePackageName: String, serviceName: String) {
        val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${classNameSuffix()}"
        val packageName = "$basePackageName.${serviceName.toCamelCase()}.${classNameSuffix().toCamelCase()}"
        val filePath = "${packageName.replace('.', '/')}/$className.kt"
        val file = codeGenerator.createNewFileByPath(
            Dependencies(false),
            filePath
        )
        val content = generateContent(packageName, className, serviceName).toByteArray()
        file.write(content)
        file.close()
    }

    fun generateContent(packageName: String, className: String, serviceName: String): String

    fun classNameSuffix(): String
}