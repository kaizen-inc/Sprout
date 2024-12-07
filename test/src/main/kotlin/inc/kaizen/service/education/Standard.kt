package inc.kaizen.service.education

import inc.kaizen.service.sprout.annotation.*
import java.util.*

@Model(
    name = "Standard",
    description = "Standard model",
    basePackageName = "inc.kaizen.service.education",
    serviceName = "standard",
    author  = "Kaizen",
    since = "2023-10-01",
    schema = "public",
    version = "1.0",
    api = StandardApi::class
)
data class Standard(
    @Id
    val id: UUID,
    val name: String,
    @Reference
    val school: School
)

@API(
    serviceName = "standard",
    description = "Standard API",
    basePackageName = "inc.kaizen.service.education",
    parents = ["school"],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = Standard::class
)
interface StandardApi {
    companion object {
        const val serviceMethod = """fun getStandardBySchool(name: String): Standard { 
val standardEntity = standardRepository.getStandardRepository(name)
return standardEntityService.convert(standardEntity)
}"""
        const val repositoryMethod = """fun getStandardRepository(name: String): StandardEntity"""
    }

    @Request(
        method = Method.GET,
        path = "/{name}",
        serviceMethod = serviceMethod,
        repositoryMethod = repositoryMethod
    )
    fun getStandardBySchoolName(name: String): Standard
}