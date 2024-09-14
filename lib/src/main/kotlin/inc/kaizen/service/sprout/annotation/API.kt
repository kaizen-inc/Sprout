package inc.kaizen.service.sprout.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class API(
    val serviceName: String,
    val description: String,
    val packageName: String,
    val version: String,
    val author: String,
    val since: String,
    val deprecated: Boolean = false,
    val authentication: Boolean = true,
    val model: KClass<*>,
)
