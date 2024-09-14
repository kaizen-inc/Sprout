package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator

class ControllerClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, serviceName: String): String {
        return buildString {
            appendLine("package $packageName.$serviceName")
            appendLine()
            appendLine("import org.springframework.beans.factory.annotation.Autowired")
            appendLine("import org.springframework.web.bind.annotation.RestController")
            appendLine("import org.springframework.web.bind.annotation.PostMapping")
            appendLine("import org.springframework.web.bind.annotation.RequestBody")
            appendLine("import org.springframework.http.ResponseEntity")
            appendLine()
            appendLine("@RestController")
            appendLine("class ${serviceName}${classNameSuffix()} {")
            appendLine()
            appendLine("    @Autowired")
            appendLine("    lateinit var entityService: EntityService")
            appendLine()
            appendLine("    @Autowired")
            appendLine("    lateinit var ${serviceName}Service: ${serviceName}Service")
            appendLine()
            appendLine("    @PostMapping(\"/$serviceName\")")
            appendLine("    override fun create(@RequestBody $serviceName: $serviceName): ResponseEntity<Any> {")
            appendLine("        return closureWithReturn {")
            appendLine("            val applyEntity = entityService.convertToEntity($serviceName)")
            appendLine("            val created = ${serviceName}Service.create(applyEntity)")
            appendLine("            return@closureWithReturn entityService.convertToDto(created)")
            appendLine("        }")
            appendLine("    }")
            appendLine()
            appendLine("}")
        }
    }

    override fun classNameSuffix() = "Controller"
}