package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.CLASS_NAME
import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

class ConverterClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val className = extensions[CLASS_NAME]

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Component")
        appendLine("import java.util.UUID")
        appendLine()
        appendLine()
        appendLine("@Component")
        appendLine("class ${className} {")
        appendLine()
        appendLine("}")
    }

    override fun classNameSuffix() = "Converter"
}