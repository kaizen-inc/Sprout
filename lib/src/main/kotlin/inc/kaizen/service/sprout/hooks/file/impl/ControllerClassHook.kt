package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.PACKAGE_NAME
import inc.kaizen.service.sprout.generator.PARENT_PATHS
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import inc.kaizen.service.sprout.hooks.method.impl.ControllerCustomMethod

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
            .classBuilder("${serviceName}Controller")
            .addSuperinterface(
                ClassName("inc.kaizen.service.sprout.base.controller", "IController")
                    .parameterizedBy(ClassName(modelPackageName, capitalizeServiceName)))
            .addAnnotation(ClassName("org.springframework.web.bind.annotation", "RestController"))

        if (parentPaths.isNotEmpty()) {
//            val path = parentPaths.map { "/${it.toString().lowercase()}s/{${it.toString().lowercase()}Id}" }
//                .joinToString(separator = "")
            builder.addAnnotation(
                ClassName("org.springframework.web.bind.annotation", "RequestMapping")
            )
        }

        return builder
    }
}