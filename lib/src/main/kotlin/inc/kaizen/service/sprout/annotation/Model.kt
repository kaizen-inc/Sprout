package inc.kaizen.service.sprout.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
annotation class Model(
    val name: String,
    val description: String,
    val basePackageName: String,
    val serviceName: String,
    val author: String,
    val since: String,
    val deprecated: Boolean = false,
    val version: String,
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Field(
    val name: String,
    val description: String,
    val required: Boolean = true,
    val deprecated: Boolean = false,
    val readOnly: Boolean = false,
    val writeOnly: Boolean = false,
    val since: String,
    val version: String
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Reference(
    val name: String,
    val description: String,
    val required: Boolean = true,
    val deprecated: Boolean = false,
    val readOnly: Boolean = false,
    val writeOnly: Boolean = false,
    val since: String,
    val version: String,
    val type: KClass<*>
)
