package inc.kaizen.service.sprout.annotation

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class API(
    val serviceName: String,
    val description: String,
    val basePackageName: String,
    val parents: Array<String> = [],
    val version: String,
    val author: String,
    val since: String,
    val deprecated: Boolean = false,
    val authentication: Boolean = true,
    val model: KClass<*>,
)

data class MethodRequest(
    val method: RequestMethod,
    val functionName: String,
    val mappingClass: KClass<out Annotation>,
) {
    companion object {
        val GET: MethodRequest = MethodRequest(RequestMethod.GET, "findById", GetMapping::class)
        val POST: MethodRequest = MethodRequest(RequestMethod.POST, "create", PostMapping::class)
        val PUT: MethodRequest = MethodRequest(RequestMethod.PUT, "update", PutMapping::class)
        val DELETE: MethodRequest = MethodRequest(RequestMethod.DELETE, "delete", DeleteMapping::class)
        val GET_ALL: MethodRequest = MethodRequest(RequestMethod.GET, "findAll", GetMapping::class)
    }
}