package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class ControllerClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackageName = extensions[BASE_PACKAGE_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = serviceName + "s"
        val className = extensions[CLASS_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.web.bind.annotation.RestController")
        appendLine("import org.springframework.web.bind.annotation.*")
        appendLine("import org.springframework.web.bind.annotation.RequestBody")
        appendLine("import org.springframework.http.ResponseEntity")
        appendLine("import $basePackageName.base.controller.IController")
        appendLine("import $basePackageName.base.service.IService")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}Service")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}EntityService")
        appendLine("import $basePackageName.base.extension.closureWithReturn")
        appendLine("import $basePackageName.base.extension.closureWithoutReturn")
        appendLine("import $basePackageName.base.extension.toUUID")
        appendLine()
        appendLine("@RestController")
        appendLine("class ${className}: IController<${capitalizeServiceName}> {")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var entityService: ${capitalizeServiceName}EntityService")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var ${serviceName}Service: ${capitalizeServiceName}Service")
        appendLine()
        appendLine("    @PostMapping(\"/$servicePluralName\")")
        appendLine("    override fun create(@RequestBody $serviceName: $capitalizeServiceName): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.create($serviceName)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @DeleteMapping(\"/$servicePluralName/{id}\")")
        appendLine("    override fun deleteById(id: String): ResponseEntity<Any> {")
        appendLine("        return closureWithoutReturn {")
        appendLine("            ${serviceName}Service.deleteById(id.toUUID())")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @GetMapping(\"/$servicePluralName/{id}\")")
        appendLine("    override fun findById(id: String): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.findById(id.toUUID())")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @GetMapping(\"/$servicePluralName\")")
        appendLine("    override fun findAll(")
        appendLine("        @RequestParam(")
        appendLine("            name=\"page\",")
        appendLine("            required = false,")
        appendLine("            defaultValue = IService.DEFAULT_PAGE_NUMBER) page: Int,")
        appendLine("        @RequestParam(")
        appendLine("            name=\"pageSize\",")
        appendLine("            required = false,")
        appendLine("            defaultValue = IService.DEFAULT_PAGE_SIZE) pageSize: Int")
        appendLine("    ): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.findAll(page, pageSize)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @PutMapping(\"/$servicePluralName\")")
        appendLine("    override fun update(t: $capitalizeServiceName): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.update(t)")
        appendLine("        }")
        appendLine("    }")
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = "Controller"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.$serviceName.controller"

    override fun className(serviceName: String) = "${serviceName.capitalizeFirstLetter()}Controller"
}