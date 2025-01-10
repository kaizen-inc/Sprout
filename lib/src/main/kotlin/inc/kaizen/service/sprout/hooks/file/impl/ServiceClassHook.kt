package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.PACKAGE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook

class ServiceClassHook: IClassHook {

    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String

        return TypeSpec
            .classBuilder("${serviceName}Service")
            .addSuperinterface(
                ClassName("inc.kaizen.service.sprout.base.service", "IService")
                    .parameterizedBy(
                        ClassName(modelPackageName, capitalizeServiceName),
                        ClassName("java.util", "UUID")
                    )
            )
    }
}