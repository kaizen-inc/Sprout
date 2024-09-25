package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

class ServiceInterfaceGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>): String = buildString {
        val packageName = extensions[PACKAGE_NAME]

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.data.domain.Page")
        appendLine()
        appendLine("interface IService<T, U> {")
        appendLine()
        appendLine("    companion object {")
        appendLine("        const val DEFAULT_PAGE_SIZE = \"10\"")
        appendLine("        const val DEFAULT_PAGE_NUMBER = \"0\"")
        appendLine("    }")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun create(t: T): T")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun deleteById(id: U)")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun update(t: T): T")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun findById(id: U): T?")
        appendLine()
        appendLine("    @Throws(Exception::class)")
        appendLine("    fun findAll(")
        appendLine("        page: Int,")
        appendLine("        pageSize: Int")
        appendLine("    ): Page<T>")
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = ""

    override fun className(serviceName: String) = "IService"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.base.service"
}