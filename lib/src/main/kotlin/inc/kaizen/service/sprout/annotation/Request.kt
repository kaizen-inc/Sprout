package inc.kaizen.service.sprout.annotation

import kotlin.annotation.AnnotationTarget.*

@Target(FUNCTION)
annotation class Request(
    val method: Method,
    val path: String,
    val serviceMethod: String = "",
    val repositoryMethod: String = "",
//    val controllerMethod: String = "" // should this be supported?
)

enum class Method {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT
}
