package inc.kaizen.service.sprout.hooks.method.impl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.SERVICE_NAME
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import java.util.UUID

class ServiceGetHook: IMethodHook {
    
    override fun hook(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        return FunSpec.builder("findById")
            .addParameter("ids", Array<UUID>::class)
            .returns(ClassName("", capitalizeServiceName))
            .addCode(
                """
            val optional = ${serviceName}Repository.findById(ids.last())
            if (optional.isPresent) {
                return ${serviceName}EntityService.convert(optional.get())
            } else {
                val message = messageSource.getMessage(
                    "Exception.noSuchElementException",
                    arrayOf(ids.last().toString()),
                    "Exception occurred",
                    LocaleContextHolder.getLocale()
                )
                throw NoSuchElementException(message)
            }
            """.trimIndent()
            )
    }
}
