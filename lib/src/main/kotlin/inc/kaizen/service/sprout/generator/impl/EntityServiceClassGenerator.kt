package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class EntityServiceClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val basePackaageName = extensions[BASE_PACKAGE_NAME]
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]

        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Service")
        appendLine("import java.util.UUID")
        appendLine("import $basePackaageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        appendLine("import $modelPackageName.${capitalizeServiceName}")
        appendLine("import $basePackaageName.$serviceName.service.${capitalizeServiceName}Service")
        appendLine("import inc.kaizen.service.sprout.base.extension.nonNullify")
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import org.springframework.core.convert.ConversionService")
        appendLine("import org.springframework.expression.ParseException")
        appendLine()
        appendLine("@Service")
        appendLine("class $className {")
        appendLine()
        appendLine("    @Autowired")
        appendLine("    private lateinit var conversionService: ConversionService")
        appendLine()
        appendLine("    @Throws(ParseException::class)")
        appendLine("    fun convert($serviceName: $capitalizeServiceName): ${capitalizeServiceName}Entity {")
        appendLine("       val ${serviceName}Entity: ${capitalizeServiceName}Entity = conversionService")
        appendLine("           .convert($serviceName, ${capitalizeServiceName}Entity::class.java)")
        appendLine("           .nonNullify()")
        appendLine("       return ${serviceName}Entity")
        appendLine("    }")
        appendLine()
        appendLine("    @Throws(ParseException::class)")
        appendLine("    fun convert(entity: ${capitalizeServiceName}Entity): $capitalizeServiceName {")
        appendLine("        val $serviceName: $capitalizeServiceName = conversionService")
        appendLine("            .convert(entity, $capitalizeServiceName::class.java)")
        appendLine("            .nonNullify()")
        appendLine("        return $serviceName")
        appendLine("    }")
        appendLine("}")
    }

    override fun classNameSuffix() = "EntityService"

    override fun packageNameSuffix() = "service"
}