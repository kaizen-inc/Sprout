package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.FunSpec
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook

class ControllerCustomMethod: IMethodHook {

    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        TODO("Not yet implemented")
    }
}