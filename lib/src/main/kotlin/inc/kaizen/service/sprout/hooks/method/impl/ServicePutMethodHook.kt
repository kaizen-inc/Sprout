package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook

class ServicePutMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val typeName = Array::class.asClassName()
            .parameterizedBy(WildcardTypeName.producerOf(String::class.asClassName()))
        return FunSpec.builder("update")
            .addParameter(serviceName, ClassName(modelPackageName, capitalizeServiceName))
            .addModifiers(KModifier.OVERRIDE)
            .returns(ClassName(modelPackageName, capitalizeServiceName))
            .addStatement("if (${serviceName}Repository.existsById(${serviceName}.id)) {")
            .addStatement("    val entity = ${serviceName}EntityService.convert(${serviceName})")
            .addStatement("    val created = ${serviceName}Repository.save(entity)")
            .addStatement("    return ${serviceName}EntityService.convert(created)")
            .addStatement("} else {")
            .addStatement("    val message = messageSource")
            .addStatement("        .getMessage(\"Exception.noSuchElementException\",")
            .addStatement("            arrayOf(${serviceName}.id.toString()),")
            .addStatement("            \"Exception occurred\",")
            .addStatement("            LocaleContextHolder.getLocale())")
            .addStatement("    throw NoSuchElementException(message)")
            .addStatement("}")
    }
}