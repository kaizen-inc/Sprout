package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.constant.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.expression.ParseException
import org.springframework.stereotype.Service

class EntityServiceClassHook: IClassHook {
    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val basePackaageName = extensions[BASE_PACKAGE_NAME] as String
        val className = "${serviceName.capitalizeFirstLetter()}EntityService"
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String

        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        val typeSpecBuilder = TypeSpec.classBuilder(className)
        typeSpecBuilder.addAnnotation(Service::class)
        typeSpecBuilder.addProperty(
            PropertySpec.builder("conversionService", ConversionService::class)
                .addModifiers(KModifier.PRIVATE, KModifier.LATEINIT)
                .mutable(true)
                .addAnnotation(Autowired::class)
                .build()
        )

        val nonNullify = MemberName("inc.kaizen.service.sprout.base.extension", "nonNullify", true)
        typeSpecBuilder.addFunction(
            FunSpec.builder("convert")
                .addAnnotation(AnnotationSpec.builder(Throws::class).addMember("%T::class", ParseException::class).build())
                .addParameter(serviceName, ClassName(modelPackageName, capitalizeServiceName))
                .returns(ClassName("$basePackaageName.$serviceName.model.entity", "${capitalizeServiceName}Entity"))
                .addStatement("val ${serviceName}Entity: ${capitalizeServiceName}Entity = " +
                        "conversionService.convert($serviceName, ${capitalizeServiceName}Entity::class.java).%M()", nonNullify)
                .addStatement("return ${serviceName}Entity")
                .build()
        )
        typeSpecBuilder.addFunction(
            FunSpec.builder("convert")
                .addAnnotation(AnnotationSpec.builder(Throws::class).addMember("%T::class", ParseException::class).build())
                .addParameter("entity", ClassName("$basePackaageName.$serviceName.model.entity", "${capitalizeServiceName}Entity"))
                .returns(ClassName(modelPackageName, capitalizeServiceName))
                .addStatement("val $serviceName: $capitalizeServiceName = conversionService.convert(entity, $capitalizeServiceName::class.java).%M()", nonNullify)
                .addStatement("return $serviceName")
                .build()
        )

        return typeSpecBuilder
    }
}