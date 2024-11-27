package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

@Deprecated("This generator class is not used anymore")
class ControllerInterfaceGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>): String = buildString {
        val packageName = extensions[PACKAGE_NAME]

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.http.ResponseEntity")
        appendLine()
        appendLine("interface IController<T> {")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun create(t: T): ResponseEntity<Any>")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun deleteById(id: String): ResponseEntity<Any>")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun update(t: T): ResponseEntity<Any>")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun findById(id: String): ResponseEntity<Any>")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun findAll(")
        appendLine("        page: Int,")
        appendLine("        pageSize: Int")
        appendLine("    ): ResponseEntity<Any>")
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = ""

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.base.controller"

    override fun className(serviceName: String) = "IController"
}