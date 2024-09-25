package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

class ExtensionClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.http.ResponseEntity")
        appendLine("import java.util.*")
        appendLine()
        appendLine("fun <T> closureWithReturn(data: () -> T): ResponseEntity<Any> {")
        appendLine("	return try {")
        appendLine("		ResponseEntity.ok(data())")
        appendLine("	} catch (e: Exception) {")
        appendLine("		ResponseEntity.internalServerError().body(e.message)")
        appendLine("	}")
        appendLine("}")
        appendLine()
        appendLine("fun closureWithoutReturn(data: () -> Unit): ResponseEntity<Any> {")
        appendLine("	return try {")
        appendLine("		data()")
        appendLine("		ResponseEntity.ok().build()")
        appendLine("	} catch (e: Exception) {")
        appendLine("		ResponseEntity.badRequest().body(e.message)")
        appendLine("	}")
        appendLine("}")
        appendLine()
        appendLine("fun String.toUUID(): UUID {")
        appendLine("	return UUID.fromString(this)")
        appendLine("}")
        appendLine()
        appendLine("fun  <T> T?.nonNullify(): T {")
        appendLine("	if(this != null) return this else throw IllegalArgumentException(\"object is null\")")
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = "Extension"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.base.extension"

    override fun className(serviceName: String) = "Extension"
}