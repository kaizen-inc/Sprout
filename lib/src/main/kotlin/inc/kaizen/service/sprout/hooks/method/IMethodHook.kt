package inc.kaizen.service.sprout.hooks.method

import com.squareup.kotlinpoet.FunSpec
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.annotation.MethodRequest

interface IMethodHook {

    fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder
}