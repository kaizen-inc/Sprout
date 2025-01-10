package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook

class RepositoryClassHook: IClassHook {

    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val modelName = serviceName.toCamelCase().capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String

        return TypeSpec
            .interfaceBuilder("${serviceName}Repository")
            .addSuperinterface(
                ClassName("org.springframework.data.jpa.repository", "JpaRepository")
                    .parameterizedBy(
                        ClassName("java.util", "UUID"),
                        ClassName(modelPackageName, modelName)
                    )
            )
    }
}