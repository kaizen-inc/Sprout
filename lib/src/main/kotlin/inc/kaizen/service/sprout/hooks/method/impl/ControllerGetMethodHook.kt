package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.constant.SERVICE_NAME
import inc.kaizen.service.sprout.constant.SERVICE_NAME_PLURAL
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

class ControllerGetMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = extensions[SERVICE_NAME_PLURAL] as String

        val closureWithReturn = MemberName("inc.kaizen.service.sprout.base.extension", "closureWithReturn")
        val toUUID = MemberName("inc.kaizen.service.sprout.base.extension", "toUUID")
        val typeName = Array::class.asClassName()
            .parameterizedBy(WildcardTypeName.producerOf(String::class.asClassName()))

        return FunSpec
            .builder(methodRequest.functionName)
            .addAnnotation(AnnotationSpec
                .builder(methodRequest.mappingClass)
                .addMember("%S", "/${servicePluralName}/{${serviceName.toCamelCase()}Id}")
                .build())
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ParameterSpec
                .builder("ids", typeName)
                .addAnnotation(AnnotationSpec
                    .builder(PathVariable::class)
                    .build())
                .build())
            .returns(ResponseEntity::class.asTypeName().parameterizedBy(Any::class.asTypeName()))
            .addCode(CodeBlock
                .builder()
                .add("return %M {", closureWithReturn)
                .add("return@closureWithReturn ${serviceName}Service.${methodRequest.functionName}(ids.map { it.%M() }.toTypedArray())", toUUID)
                .add("}")
                .build())
    }
}