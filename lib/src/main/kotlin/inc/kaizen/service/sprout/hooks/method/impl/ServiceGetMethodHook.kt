package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import java.util.UUID

class ServiceGetMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        val modelPackageName = extensions[MODEL_PACKAGE_NAME] as String
        val typeName = Array::class.asClassName()
            .parameterizedBy(WildcardTypeName.producerOf(UUID::class.asClassName()))
        val className = ClassName("org.springframework.context.i18n", "LocaleContextHolder")
        return FunSpec
            .builder(methodRequest.functionName)
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("ids", typeName)
            .returns(ClassName(modelPackageName, capitalizeServiceName))
            .addStatement("val optional = ${serviceName}Repository.${methodRequest.functionName}(ids.last())")
            .addStatement("if (optional.isPresent) {")
            .addStatement("    return ${serviceName}EntityService.convert(optional.get())")
            .addStatement("} else {")
            .addStatement("    val message = messageSource.getMessage(")
            .addStatement("        \"Exception.noSuchElementException\", arrayOf(ids.last().toString()),")
            .addStatement("        \"Exception occurred\", %T.getLocale())", className)
            .addStatement("    throw NoSuchElementException(message)")
            .addStatement("}")
    }
}
