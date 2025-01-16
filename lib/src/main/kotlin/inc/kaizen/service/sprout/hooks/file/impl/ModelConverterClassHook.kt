package inc.kaizen.service.sprout.hooks.file.impl

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.generator.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import org.mapstruct.Mapper
import java.util.*

class ModelConverterClassHook: IClassHook {
    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val className = "${serviceName.capitalizeFirstLetter()}Converter"
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String
        val basePackaageName = extensions[BASE_PACKAGE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        val modelClass = extensions["model"] as KSClassDeclaration
        val complexFields = modelClass.findComplexType()

        return TypeSpec.classBuilder(className)
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
                        ClassName("$basePackaageName.$serviceName.entity", "${capitalizeServiceName}Entity"),
                        ClassName(modelPackageName, capitalizeServiceName)
                    )
            )
            .addFunctions(
                complexFields.map {
                    FunSpec.builder("fetch${it.type}")
                        .addParameter("entity", ClassName("$basePackaageName.$serviceName.entity", "${capitalizeServiceName}Entity"))
                        .returns(UUID::class.asClassName())
                        .addStatement("return entity.id")
                        .build()
                }.toList()
            )
            .addFunction(
                FunSpec.builder("convert")
                    .addModifiers(KModifier.ABSTRACT, KModifier.OVERRIDE)
                    .addParameter("entity", ClassName("$basePackaageName.$serviceName.entity", "${capitalizeServiceName}Entity"))
                    .returns(ClassName(modelPackageName, capitalizeServiceName))
                    .build()
            )
    }
}