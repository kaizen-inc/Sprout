package inc.kaizen.service.sprout.generator.impl

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import inc.kaizen.service.sprout.annotation.Request
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
        val parentPaths = extensions[PARENT_PATHS] as List<*>

        val extensionFunctions = extensions[EXTENSION_METHODS] as List<*>

        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.web.bind.annotation.RestController")
        appendLine("import org.springframework.web.bind.annotation.*")
        appendLine("import org.springframework.web.bind.annotation.RequestBody")
        appendLine("import org.springframework.http.ResponseEntity")
        appendLine("import inc.kaizen.service.sprout.base.controller.IController")
        appendLine("import inc.kaizen.service.sprout.base.service.IService")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}Service")
        appendLine("import $basePackageName.$serviceName.service.${capitalizeServiceName}EntityService")
        appendLine("import inc.kaizen.service.sprout.base.extension.closureWithReturn")
        appendLine("import inc.kaizen.service.sprout.base.extension.closureWithoutReturn")
        appendLine("import inc.kaizen.service.sprout.base.extension.toUUID")
        appendLine()
        appendLine("@RestController")
        appendLine("@Suppress(\"PARAMETER_NAME_CHANGED_ON_OVERRIDE\")")
        if(parentPaths.isNotEmpty()) {
            appendLine("@RequestMapping(\"${parentPaths.map { "/${it.toString()
                .lowercase()}s/{${it.toString().lowercase()}Id}" }.joinToString(separator = "")}\")")
        }
        appendLine("class ${className}: IController<${capitalizeServiceName}> {")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var entityService: ${capitalizeServiceName}EntityService")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    lateinit var ${serviceName}Service: ${capitalizeServiceName}Service")
        appendLine()
        appendLine("    @PostMapping(\"/$servicePluralName\")")
        appendLine("    override fun create(")
        appendLine("        @RequestBody $serviceName: $capitalizeServiceName")
        appendLine("    ): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.create($serviceName)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @DeleteMapping(\"/$servicePluralName/{${serviceName}Id}\")")
        appendLine("    override fun deleteById(")
        appendLine("        @PathVariable ids: Array<out String>")
        appendLine("    ): ResponseEntity<Any> {")
        appendLine("        return closureWithoutReturn {")
        appendLine("            ${serviceName}Service.deleteById(id.map { it.toUUID() }.toTypedArray())")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    @GetMapping(\"/$servicePluralName/{${serviceName}Id}\")")
        appendLine("    override fun findById(")
        appendLine("        @PathVariable ids: Array<out String>")
        appendLine("    ): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.findById(id.map { it.toUUID() }.toTypedArray())")
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
        appendLine("    override fun update(")
        appendLine("        @RequestBody $serviceName: $capitalizeServiceName")
        appendLine("    ): ResponseEntity<Any> {")
        appendLine("        return closureWithReturn {")
        appendLine("            return@closureWithReturn ${serviceName}Service.update($serviceName)")
        appendLine("        }")
        appendLine("    }")
        extensionFunctions.forEach { function ->
            appendLine()
            if (function is KSFunctionDeclaration)
                appendLine(function.toRequestMapping(serviceName))
        }
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = "Controller"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.$serviceName.controller"

    override fun className(serviceName: String) = "${serviceName.capitalizeFirstLetter()}Controller"

    private fun KSFunctionDeclaration.toRequestMapping(serviceName: String): String {
        val requestAnnotation = annotations.find { it.shortName.asString() == Request::class.simpleName }!!

        val extensions = requestAnnotation
            .arguments
            .associate { it.name?.asString() to it.value }
            .filterValues { it != null }
            .mapValues { it.value!! }
            .mapKeys { it.key!! }
            .toMutableMap()

        val path = extensions["path"] as String

        return buildString {
            appendLine("    @GetMapping(\"$path\")")
            appendLine("    fun ${simpleName.asString()}(")
            parameters.forEach { parameter ->
                appendLine("        @PathVariable ${parameter}: ${parameter.type}")
            }
            appendLine("    ): ResponseEntity<Any> {")
            appendLine("        return closureWithReturn {")
            appendLine("            return@closureWithReturn ${serviceName}Service.${simpleName.asString()}(")
            parameters.forEachIndexed { index, parameter ->
                appendLine("                ${parameter} = ${parameter}")
                if (index < parameters.size - 1) {
                    appendLine(",")
                }
            }
            appendLine("            )")
            appendLine("        }")
            appendLine("    }")
        }
    }
}