package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import java.util.UUID

class ServiceDeleteMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val typeName = Array::class.asClassName()
            .parameterizedBy(WildcardTypeName.producerOf(UUID::class.asClassName()))
        return FunSpec
            .builder(methodRequest.functionName)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("ids", typeName)
            .addStatement("${serviceName}Repository.${methodRequest.functionName}(ids.last())")
    }
}