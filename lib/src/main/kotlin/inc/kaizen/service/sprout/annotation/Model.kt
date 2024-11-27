package inc.kaizen.service.sprout.annotation

import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(CLASS, FIELD)
annotation class Model(
    val name: String,
    val description: String,
    val basePackageName: String,
    val serviceName: String,
    val author: String,
    val schema: String,
    val since: String,
    val deprecated: Boolean = false,
    val version: String,
    val api: KClass<*>
)

@Target(FIELD)
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

@Target(PROPERTY, FIELD, PROPERTY_SETTER, PROPERTY_GETTER)
annotation class Reference()

@Target(PROPERTY, FIELD, PROPERTY_SETTER, PROPERTY_GETTER)
annotation class Id()
