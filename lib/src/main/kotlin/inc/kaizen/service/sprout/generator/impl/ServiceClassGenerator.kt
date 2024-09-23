package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.IClassContentGenerator

class ServiceClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String): String {
        return buildString {
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
            appendLine("import $packageName.repository.${capitalizeServiceName}Repository")
            appendLine()
            appendLine()
            appendLine("@Service")
            appendLine("class ${className} {")
            appendLine()
            appendLine("    @Autowired")
            appendLine("    private lateinit var ${serviceName}Repository: ${capitalizeServiceName}Repository")
            appendLine()
            appendLine("    @Autowired")
            appendLine("    private lateinit var messageSource: MessageSource")
            appendLine()
            appendLine("    override fun create(${serviceName}: ${capitalizeServiceName}): ${capitalizeServiceName} {")
            appendLine("        return ${serviceName}Repository.save(${serviceName})")
            appendLine("    }")
            appendLine()
            appendLine("    override fun deleteById(id: UUID) {")
            appendLine("        ${serviceName}Repository.deleteById(id)")
            appendLine("    }")
            appendLine()
            appendLine("    override fun update(${serviceName}: ${capitalizeServiceName}): ${capitalizeServiceName} {")
            appendLine("        if (${serviceName}Repository.existsById(${serviceName}.id)) {")
            appendLine("            return ${serviceName}Repository.save(${serviceName})")
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
            appendLine("        return ${serviceName}Repository.findById(id).orElseGet { null }")
            appendLine("    }")
            appendLine()
            appendLine("    override fun findAll(")
            appendLine("        page: Int,")
            appendLine("        pageSize: Int")
            appendLine("    ): Page<${capitalizeServiceName}> {")
            appendLine("        val pageable = PageRequest.of(page, pageSize)")
            appendLine("        return ${serviceName}Repository.findAll(pageable)")
            appendLine("    }")
            appendLine("}")
            appendLine()
        }
    }

    override fun classNameSuffix() = "Service"
}