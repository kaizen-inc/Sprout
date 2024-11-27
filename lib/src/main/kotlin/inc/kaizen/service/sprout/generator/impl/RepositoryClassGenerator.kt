package inc.kaizen.service.sprout.generator.impl

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class RepositoryClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val basePackaageName = extensions[BASE_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val extensionFunctions = extensions[EXTENSION_METHODS] as List<*>

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.data.jpa.repository.JpaRepository")
        appendLine("import org.springframework.stereotype.Repository")
        appendLine("import java.util.UUID")
        appendLine("import $basePackaageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        appendLine()
        appendLine("@Repository")
        appendLine("interface ${className}: JpaRepository<${capitalizeServiceName}Entity, UUID> {")
        extensionFunctions.forEach { function ->
            appendLine()
            if (function is KSFunctionDeclaration)
                appendLine(toRequestMapping(function))
        }
        appendLine("}")
    }

    override fun classNameSuffix() = "Repository"

    private fun toRequestMapping(function: KSFunctionDeclaration): String {
        return buildString {
            appendLine("  fun ${function}(")
            function.parameters.forEachIndexed { index, parameter ->
                appendLine("    ${parameter}: ${parameter.type}")
                if (index < function.parameters.size - 1) appendLine(",")
            }
            appendLine("  ): ${function.returnType}Entity")
        }
    }
}