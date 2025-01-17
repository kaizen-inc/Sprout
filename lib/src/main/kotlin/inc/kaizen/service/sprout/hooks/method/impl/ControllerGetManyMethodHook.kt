package inc.kaizen.service.sprout.hooks.file.impl

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.base.service.IService
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.generator.SERVICE_NAME_PLURAL
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam

class ControllerGetManyMethodHook: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val servicePluralName = extensions[SERVICE_NAME_PLURAL] as String
        val closureWithReturn = MemberName("inc.kaizen.service.sprout.base.extension", "closureWithReturn")
        return FunSpec
            .builder(methodRequest.functionName)
            .addAnnotation(AnnotationSpec
                .builder(methodRequest.mappingClass)
                .addMember("%S", servicePluralName)
                .build())
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(ParameterSpec.builder("page", Int::class)
                .addAnnotation(AnnotationSpec.builder(RequestParam::class)
                    .addMember("name = %S", "page")
                    .addMember("required = false")
                    .addMember("defaultValue = %S", IService.DEFAULT_PAGE_NUMBER)
                    .build())
                .build())
            .addParameter(ParameterSpec.builder("pageSize", Int::class)
                .addAnnotation(AnnotationSpec.builder(RequestParam::class)
                    .addMember("name = %S", "pageSize")
                    .addMember("required = false")
                    .addMember("defaultValue = %S", IService.DEFAULT_PAGE_SIZE)
                    .build())
                .build())
            .returns(ResponseEntity::class.asTypeName().parameterizedBy(Any::class.asTypeName()))
            .addCode(CodeBlock
                .builder()
                .add("return %M {", closureWithReturn)
                .addStatement("    return@closureWithReturn ${serviceName}Service.${methodRequest.functionName}(page, pageSize)")
                .add("}")
                .build())
    }
}