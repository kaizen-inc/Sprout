package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.constant.SERVICE_NAME
import inc.kaizen.service.sprout.constant.SERVICE_NAME_PLURAL
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable

class ControllerDeleteMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = extensions[SERVICE_NAME_PLURAL] as String
        val closureWithoutReturn = MemberName("inc.kaizen.service.sprout.base.extension", "closureWithoutReturn")
        val toUUID = MemberName("inc.kaizen.service.sprout.base.extension", "toUUID")
        val typeName = Array::class.asClassName()
            .parameterizedBy(WildcardTypeName.producerOf(String::class.asClassName()))

        return FunSpec
            .builder(methodRequest.functionName)
            .addAnnotation(
                AnnotationSpec.builder(DeleteMapping::class)
                    .addMember("\"/$servicePluralName/{${serviceName}Id}\"")
                    .build()
            )
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(
                ParameterSpec.builder("ids", typeName)
                    .addAnnotation(PathVariable::class)
                    .build()
            )
            .returns(ResponseEntity::class.asClassName().parameterizedBy(Any::class.asTypeName()))
            .addCode(CodeBlock
                .builder()
                .add("return %M {", closureWithoutReturn)
                .add("return@closureWithoutReturn ${serviceName}Service.${methodRequest.functionName}(ids.map { it.%M() }.toTypedArray())", toUUID)
                .add("}")
                .build())
    }
}