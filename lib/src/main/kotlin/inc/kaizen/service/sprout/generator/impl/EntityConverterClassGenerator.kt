package inc.kaizen.service.sprout.generator.impl

import com.google.devtools.ksp.symbol.KSClassDeclaration
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.*

class EntityConverterClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val basePackaageName = extensions[BASE_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        val modelClass = extensions["model"] as KSClassDeclaration
        val complexFields = modelClass.findComplexType()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.stereotype.Component")
        appendLine("import java.util.UUID")
        appendLine("import org.mapstruct.Mapper")
        appendLine("import org.mapstruct.Mapping")
        appendLine("import org.mapstruct.Mappings")
        appendLine("import org.springframework.core.convert.converter.Converter")
        appendLine("import org.springframework.beans.factory.annotation.Autowired")
        appendLine("import $modelPackageName.$capitalizeServiceName")
        appendLine("import $basePackaageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        complexFields.iterator().forEach {
            appendLine("import $modelPackageName.${it.type}")
            appendLine("import $basePackaageName.${it.type.toString().toCamelCase()}.repository.${it.type}Repository")
            appendLine("import $basePackaageName.${it.type.toString().toCamelCase()}.model.entity.${it.type}Entity")
        }
        appendLine()
        appendLine("@Mapper(componentModel = \"spring\"/*, config = MappingConfiguration::class*/)")
        appendLine("abstract class ${className}: Converter<${capitalizeServiceName}, ${capitalizeServiceName}Entity> {")
        appendLine()
        complexFields.asIterable().forEach {
            appendLine("    @Autowired")
            appendLine("    private lateinit var ${it.type.toString().toCamelCase()}Repository: ${it.type}Repository")
            appendLine()
        }
        complexFields.asIterable().forEach {
            appendLine("    @Mappings(")
            appendLine("        Mapping(target = \"${it}\", expression = \"java(fetch${it.type}($it.get${it.type}()))\")")
            appendLine("    )")
        }
        appendLine("    abstract override fun convert(entity: ${capitalizeServiceName}): ${capitalizeServiceName}Entity")
        appendLine()
        complexFields.asIterable().forEach {
            appendLine("    protected fun fetch${it.type}(entity: ${it.type}): ${it.type}Entity {")
            appendLine("        return ${it.type.toString().toCamelCase()}Repository.findById(entity.id).orElse(null)")
            appendLine("    }")
            appendLine()
        }
        appendLine("}")
    }

    override fun classNameSuffix() = "EntityConverter"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.$serviceName.converter"
}