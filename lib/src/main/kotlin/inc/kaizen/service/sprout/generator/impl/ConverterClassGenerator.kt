package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator

class ConverterClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String): String {
        return buildString {
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
    }

    override fun classNameSuffix() = "Converter"
}