package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME_PLURAL
import org.springframework.http.ResponseEntity

class ControllerGetHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = extensions[SERVICE_NAME_PLURAL] as String

        return FunSpec
            .builder(methodRequest.functionName)
            .addAnnotation(AnnotationSpec
                .builder(methodRequest.mappingClass)
                .addMember("%S", "/${servicePluralName}/{${serviceName.toCamelCase()}Id}")
                .build())
//            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ParameterSpec.builder("ids", Array<String>::class).build())
            .returns(ResponseEntity::class.asTypeName().parameterizedBy(Any::class.asTypeName()))
//            .addStatement("return closureWithReturn {")
//            .addStatement("    return@closureWithReturn ${serviceName}Service.${methodRequest.functionName}(ids.map { it.toUUID() }.toTypedArray())")
//            .addStatement("}")
    }
}