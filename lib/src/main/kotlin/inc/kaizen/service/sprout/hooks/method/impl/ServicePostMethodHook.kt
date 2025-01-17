package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook

class ServicePostMethodHook: IMethodHook {

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
            .addParameter(serviceName, ClassName(modelPackageName, capitalizeServiceName))
            .returns(ClassName(modelPackageName, capitalizeServiceName))
            .addStatement("val entity = ${serviceName}EntityService.convert($serviceName)")
            .addStatement("val created = ${serviceName}Repository.save(entity)")
            .addStatement("return ${serviceName}EntityService.convert(created)")
    }
}