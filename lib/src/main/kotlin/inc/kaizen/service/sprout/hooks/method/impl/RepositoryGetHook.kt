package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook

class RepositoryGetHook: IMethodHook {
    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions["serviceName"] as String
        return FunSpec.builder("get")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("id", Long::class)
            .addStatement("return repository.findById(id)")
//                    ".orElseThrow { NotFoundException(\"%s not found with id \\%d\", \"${serviceName.toCamelCase()}\", id) }")
    }
}