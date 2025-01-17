package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook

class ServiceGetManyMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions["serviceName"] as String
        val modelPackageName = extensions["modelPackageName"] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        return FunSpec.builder(methodRequest.functionName)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("page", Int::class)
            .addParameter("pageSize", Int::class)
            .returns(
                ClassName("org.springframework.data.domain", "Page")
                    .parameterizedBy(ClassName(modelPackageName, capitalizeServiceName))
            )
            .addStatement("val pageable = ${ClassName("org.springframework.data.domain", "PageRequest")}.of(page, pageSize)")
            .addStatement("val entities = ${serviceName}Repository.${methodRequest.functionName}(pageable)")
            .addStatement("return entities.map { ${serviceName}EntityService.convert(it) }")
    }
}