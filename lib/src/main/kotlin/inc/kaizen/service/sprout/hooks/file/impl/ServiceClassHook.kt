package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.PACKAGE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import java.util.*

class ServiceClassHook: IClassHook {

    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String

        return TypeSpec
            .classBuilder("${capitalizeServiceName}Service")
            .addSuperinterface(
                ClassName("inc.kaizen.service.sprout.base.service", "IService")
                    .parameterizedBy(
                        ClassName(modelPackageName, capitalizeServiceName),
                        UUID::class.asClassName()
                    )
            )
            .addProperty(
                PropertySpec
                    .builder("${serviceName}Repository", ClassName("${modelPackageName}.${serviceName}.repository", "${capitalizeServiceName}Repository"))
                    .addAnnotation(ClassName("org.springframework.beans.factory.annotation", "Autowired"))
                    .mutable(true)
                    .addModifiers(KModifier.LATEINIT)
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("${serviceName}EntityService", ClassName("${modelPackageName}.${serviceName}.service", "${capitalizeServiceName}EntityService"))
                    .addAnnotation(ClassName("org.springframework.beans.factory.annotation", "Autowired"))
                    .mutable(true)
                    .addModifiers(KModifier.LATEINIT)
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("messageSource", ClassName("org.springframework.context", "MessageSource"))
                    .addAnnotation(ClassName("org.springframework.beans.factory.annotation", "Autowired"))
                    .mutable(true)
                    .addModifiers(KModifier.LATEINIT)
                    .build()
            )
    }
}