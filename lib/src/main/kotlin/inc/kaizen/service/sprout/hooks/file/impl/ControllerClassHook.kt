package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.constant.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.constant.PARENT_PATHS
import inc.kaizen.service.sprout.constant.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook

class ControllerClassHook: IClassHook {

    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String
        val parentPaths = extensions[PARENT_PATHS] as List<*>

        val builder = TypeSpec
            .classBuilder("${capitalizeServiceName}Controller")
            .addSuperinterface(
                ClassName("inc.kaizen.service.sprout.base.controller", "IController")
                    .parameterizedBy(ClassName(modelPackageName, capitalizeServiceName)))
            .addAnnotation(ClassName("org.springframework.web.bind.annotation", "RestController"))
            .addAnnotation(AnnotationSpec.builder(ClassName("kotlin", "Suppress"))
                .addMember("\"PARAMETER_NAME_CHANGED_ON_OVERRIDE\"")
                .build())
            .addProperty(
                PropertySpec
                    .builder("${serviceName}Service", ClassName("${modelPackageName}.${serviceName}.service", "${capitalizeServiceName}Service"))
                    .addAnnotation(ClassName("org.springframework.beans.factory.annotation", "Autowired"))
                    .mutable(true)
                    .addModifiers(KModifier.LATEINIT)
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("entityService", ClassName("${modelPackageName}.${serviceName}.service", "${capitalizeServiceName}EntityService"))
                    .addAnnotation(ClassName("org.springframework.beans.factory.annotation", "Autowired"))
                    .mutable(true)
                    .addModifiers(KModifier.LATEINIT)
                    .build()
            )
        if (parentPaths.isNotEmpty()) {
            val paths = parentPaths.map { "/${it.toString().lowercase()}s/{${it.toString().lowercase()}Id}" }
                .joinToString(separator = "")
            builder.addAnnotation(
                AnnotationSpec.builder(ClassName("org.springframework.web.bind.annotation", "RequestMapping"))
                    .addMember("\"$paths\"")
                    .build()
            )
        }

        return builder
    }
}