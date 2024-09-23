package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator

class BaseModelClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String) = buildString {
        appendLine("package $packageName")
        appendLine()
        appendLine("import java.util.*")
        appendLine()
        appendLine("abstract class Base(")
        appendLine("    var created: Date = Date(),")
        appendLine("    var modified: Date = Date(),")
        appendLine(")")
    }

    override fun classNameSuffix() = ""
}