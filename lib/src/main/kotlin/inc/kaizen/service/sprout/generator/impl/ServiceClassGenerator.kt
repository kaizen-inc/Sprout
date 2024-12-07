package inc.kaizen.service.sprout.generator.impl

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import inc.kaizen.service.sprout.annotation.Request
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.findArgumentByName
import inc.kaizen.service.sprout.generator.*

class ServiceClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackageName = extensions[BASE_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val extensionFunctions = extensions[EXTENSION_METHODS] as List<*>

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Service")
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.context.MessageSource")
        appendLine("import org.springframework.context.i18n.LocaleContextHolder")
        appendLine("import org.springframework.data.domain.Page")
        appendLine("import org.springframework.data.domain.PageRequest")
        appendLine("import java.util.UUID")
        appendLine("import inc.kaizen.service.sprout.base.service.IService")
        appendLine("import $basePackageName.$serviceName.repository.${capitalizeServiceName}Repository")
        appendLine("import $basePackageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine()
        appendLine("@Suppress(\"PARAMETER_NAME_CHANGED_ON_OVERRIDE\")")
        appendLine("@Service")
        appendLine("class ${className}: IService<${capitalizeServiceName}, UUID>{")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    private lateinit var ${serviceName}Repository: ${capitalizeServiceName}Repository")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    private lateinit var ${serviceName}EntityService: ${capitalizeServiceName}EntityService")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    private lateinit var messageSource: MessageSource")
        appendLine()
        appendLine("    override fun create(${serviceName}: ${capitalizeServiceName}): ${capitalizeServiceName} {")
        appendLine("        val entity = ${serviceName}EntityService.convert(${serviceName})")
        appendLine("        val created = ${serviceName}Repository.save(entity)")
        appendLine("        return ${serviceName}EntityService.convert(created)")
        appendLine("    }")
        appendLine()
        appendLine("    override fun deleteById(")
        appendLine("        ids: Array<out UUID>")
        appendLine("    ) {")
        appendLine("        ${serviceName}Repository.deleteById(ids.last())")
        appendLine("    }")
        appendLine()
        appendLine("    override fun update(${serviceName}: ${capitalizeServiceName}): ${capitalizeServiceName} {")
        appendLine("        if (${serviceName}Repository.existsById(${serviceName}.id)) {")
        appendLine("            val entity = ${serviceName}EntityService.convert(${serviceName})")
        appendLine("            val created = ${serviceName}Repository.save(entity)")
        appendLine("            return ${serviceName}EntityService.convert(created)")
        appendLine("        } else {")
        appendLine("            val message = messageSource")
        appendLine("                .getMessage(\"Exception.noSuchElementException\",")
        appendLine("                    arrayOf(${serviceName}.id.toString()),")
        appendLine("                    \"Exception occurred\",")
        appendLine("                    LocaleContextHolder.getLocale())")
        appendLine("            throw NoSuchElementException(message)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    override fun findById(")
        appendLine("        ids: Array<out UUID>")
        appendLine("    ): ${capitalizeServiceName}? {")
        appendLine("        val optional = ${serviceName}Repository.findById(ids.last())")
        appendLine("        if (optional.isPresent) {")
        appendLine("            return ${serviceName}EntityService.convert(optional.get())")
        appendLine("        } else {")
        appendLine("            val message = messageSource")
        appendLine("                .getMessage(\"Exception.noSuchElementException\",")
        appendLine("                    arrayOf(ids.last().toString()),")
        appendLine("                    \"Exception occurred\",")
        appendLine("                    LocaleContextHolder.getLocale())")
        appendLine("            throw NoSuchElementException(message)")
        appendLine("        }")
        appendLine("    }")
        appendLine()
        appendLine("    override fun findAll(")
        appendLine("        page: Int,")
        appendLine("        pageSize: Int")
        appendLine("    ): Page<${capitalizeServiceName}> {")
        appendLine("        val pageable = PageRequest.of(page, pageSize)")
        appendLine("        val entities = ${serviceName}Repository.findAll(pageable)")
        appendLine("        return entities.map { ${serviceName}EntityService.convert(it) }")
        appendLine("    }")
        extensionFunctions.forEach { function ->
            appendLine()
            if (function is KSFunctionDeclaration) {
                val customServiceImplementation = function.annotations
                    .find { it.shortName.asString() == Request::class.simpleName }
                    ?.let { annotation ->
                        annotation.findArgumentByName("serviceMethod")?.value
                    }
                if (customServiceImplementation != null && customServiceImplementation.toString().isNotEmpty()) {
                    appendLine(customServiceImplementation.toString()) //TODO: format this code, it's ugly
                } else {
                    appendLine(toRequestMapping(function, serviceName))
                }
            }
        }
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = "Service"

    private fun toRequestMapping(function: KSFunctionDeclaration, serviceName: String): String {
        return buildString {
            appendLine("  fun ${function}(")
            function.parameters.forEachIndexed { index, parameter ->
                appendLine("    ${parameter}: ${parameter.type}${if (index < function.parameters.size - 1) "," else ""}")
            }
            appendLine("  ): ${function.returnType} {")
            appendLine("    val entity = ${serviceName}Repository.${function}(")
            function.parameters.forEachIndexed { index, parameter ->
                appendLine("      ${parameter} = ${parameter}${if (index < function.parameters.size - 1) "," else ""}")
            }
            appendLine("    )")
            appendLine("    return ${serviceName}EntityService.convert(entity)")
            appendLine("  }")
        }
    }
}