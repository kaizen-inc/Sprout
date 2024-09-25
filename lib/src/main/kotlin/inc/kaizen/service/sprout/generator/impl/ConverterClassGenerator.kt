package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class ConverterClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Component")
        appendLine("import java.util.UUID")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine()
        appendLine()
        appendLine("@Component")
        appendLine("class ${className} {")
        appendLine()
        appendLine("}")
    }

    override fun classNameSuffix() = "Converter"
}