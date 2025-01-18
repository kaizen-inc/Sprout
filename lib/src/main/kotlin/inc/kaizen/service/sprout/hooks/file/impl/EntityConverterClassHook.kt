package inc.kaizen.service.sprout.hooks.file.impl

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.constant.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

class EntityConverterClassHook: IClassHook {
    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val className = "${serviceName.capitalizeFirstLetter()}EntityConverter"
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String
        val basePackaageName = extensions[BASE_PACKAGE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        val modelClass = extensions["model"] as KSClassDeclaration
        val complexFields = modelClass.findComplexType()

        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.ABSTRACT)
            .addAnnotation(
                AnnotationSpec.builder(Mapper::class)
                    .addMember("componentModel = %S", "spring")
                    .addMember("config = %T::class", ClassName("inc.kaizen.service.sprout.base.configuration", "MappingConfiguration"))
                    .build()
            )
            .addSuperinterface(
                ClassName("org.springframework.core.convert.converter", "Converter")
                    .parameterizedBy(
                        ClassName(modelPackageName, capitalizeServiceName),
                        ClassName("$basePackaageName.$serviceName.model.entity", "${capitalizeServiceName}Entity")
                    )
            )

        complexFields.forEach {
            classBuilder.addProperty(
                PropertySpec.builder("${it.type.toString().toCamelCase()}Repository", ClassName("$basePackaageName.${it.type.toString().toCamelCase()}.repository", "${it.type}Repository"))
                    .addModifiers(KModifier.PRIVATE, KModifier.LATEINIT)
                    .mutable(true)
                    .addAnnotation(Autowired::class)
                    .build()
            )
        }

        complexFields.forEach {
            val name = it.type.toString().toCamelCase()
            classBuilder.addFunction(
                FunSpec.builder("fetch${it.type}")
                    .addModifiers(KModifier.PROTECTED)
                    .addParameter(name, ClassName(modelPackageName, it.type.toString()))
                    .returns(ClassName("$basePackaageName.$name.model.entity", "${it.type}Entity"))
                    .addStatement("return ${name}Repository.findById(${name}.id).orElse(null)")
                    .build()
            )
        }

        classBuilder.addFunction(
            FunSpec.builder("convert")
                .addModifiers(KModifier.ABSTRACT, KModifier.OVERRIDE)
                .addParameter(serviceName, ClassName(modelPackageName, capitalizeServiceName))
                .returns(ClassName("$basePackaageName.$serviceName.model.entity", "${capitalizeServiceName}Entity"))
                .build()
        )

        return classBuilder
    }
}