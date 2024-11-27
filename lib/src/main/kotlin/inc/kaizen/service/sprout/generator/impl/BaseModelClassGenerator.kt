package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

@Deprecated("This generator class is not used anymore")
class BaseModelClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]

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

    override fun packageNameSuffix() = "base"

    override fun className(serviceName: String) = "Base"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.base.model.data"
}