package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.CLASS_NAME
import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME

class EntityServiceClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Service")
        appendLine("import java.util.UUID")
        appendLine()
        appendLine("@Service")
        appendLine("class $className {")
        appendLine()
        appendLine("    fun convertToEntity($serviceName: $capitalizeServiceName) {")
        appendLine("            // TODO: Implement this method")
        appendLine("    }")
        appendLine()
        appendLine("    fun convertToDto(entity: ${capitalizeServiceName}Entity) {")
        appendLine("        // TODO: Implement this method")
        appendLine("    }")
        appendLine("}")
    }

    override fun classNameSuffix() = "EntityService"

    override fun packageNameSuffix() = "service"
}