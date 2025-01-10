package inc.kaizen.service.sprout.hooks.file

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.hooks.Component

interface IClassHook {
    fun hook(component: Component, extensions: Map<String, Any>): TypeSpec.Builder
}