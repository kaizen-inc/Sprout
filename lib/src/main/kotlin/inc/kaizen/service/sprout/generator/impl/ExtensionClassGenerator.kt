package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator

class ExtensionClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String) = buildString {
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
    }

    override fun classNameSuffix() = "Extension"
}