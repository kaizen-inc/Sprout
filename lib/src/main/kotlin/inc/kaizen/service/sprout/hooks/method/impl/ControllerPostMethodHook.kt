package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.constant.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.constant.SERVICE_NAME
import inc.kaizen.service.sprout.constant.SERVICE_NAME_PLURAL
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

class ControllerPostMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = extensions[SERVICE_NAME_PLURAL] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String

        return FunSpec
            .builder(methodRequest.functionName)
            .addAnnotation(
                AnnotationSpec.builder(PostMapping::class)
                    .addMember("\"/$servicePluralName\"")
                    .build()
            )
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(
                ParameterSpec.builder(serviceName, ClassName(modelPackageName, capitalizeServiceName))
                    .addAnnotation(RequestBody::class)
                    .build()
            )
            .returns(ResponseEntity::class.asClassName().parameterizedBy(Any::class.asTypeName()))
            .addCode(
                """
            return closureWithReturn {
                return@closureWithReturn ${serviceName}Service.create($serviceName)
            }
            """.trimIndent()
            )
    }
}