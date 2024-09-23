package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class ControllerClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackageName = extensions[BASE_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.web.bind.annotation.RestController")
        appendLine("import org.springframework.web.bind.annotation.PostMapping")
        appendLine("import org.springframework.web.bind.annotation.RequestBody")
        appendLine("import org.springframework.http.ResponseEntity")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}Service")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}EntityService")
        appendLine("import $basePackageName.$serviceName.extension.closureWithReturn")
        appendLine()
        appendLine("@RestController")
        appendLine("class ${className} {")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var entityService: ${capitalizeServiceName}EntityService")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var ${serviceName}Service: ${capitalizeServiceName}Service")
        appendLine()
        appendLine("    @PostMapping(\"/$serviceName\")")
        appendLine("    override fun create(@RequestBody $serviceName: $capitalizeServiceName): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            val applyEntity = entityService.convertToEntity($serviceName)")
        appendLine("            val created = ${serviceName}Service.create(applyEntity)")
        appendLine("            return@closureWithReturn entityService.convertToDto(created)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("}")
    }

    override fun classNameSuffix() = "Controller"
}