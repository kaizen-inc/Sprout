package inc.kaizen.service.sprout.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

interface IClassContentGenerator {

    fun generate(codeGenerator: CodeGenerator, packageName: String, serviceName: String) {
        val file = codeGenerator.createNewFile(
            Dependencies(
                false
            ),
            packageName,
            "${serviceName}${classNameSuffix()}"
        )
        val content = generateContent(packageName, serviceName).toByteArray()
        file.write(content)
        file.close()
    }

    fun generateContent(packageName: String, serviceName: String): String

    fun classNameSuffix(): String
}