package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class ServiceClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackaageName = extensions[BASE_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Service")
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.context.MessageSource")
        appendLine("import org.springframework.context.i18n.LocaleContextHolder")
        appendLine("import org.springframework.data.domain.Page")
        appendLine("import org.springframework.data.domain.PageRequest")
        appendLine("import java.util.UUID")
        appendLine("import $basePackaageName.base.service.IService")
        appendLine("import $basePackaageName.$serviceName.repository.${capitalizeServiceName}Repository")
        appendLine("import $basePackaageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine()
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
        appendLine("    override fun deleteById(id: UUID) {")
        appendLine("        ${serviceName}Repository.deleteById(id)")
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
        appendLine("    override fun findById(id: UUID): ${capitalizeServiceName}? {")
        appendLine("        val optional = ${serviceName}Repository.findById(id)")
        appendLine("        if (optional.isPresent) {")
        appendLine("            return ${serviceName}EntityService.convert(optional.get())")
        appendLine("        } else {")
        appendLine("            val message = messageSource")
        appendLine("                .getMessage(\"Exception.noSuchElementException\",")
        appendLine("                    arrayOf(id.toString()),")
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
        appendLine("}")
        appendLine()
    }

    override fun classNameSuffix() = "Service"
}