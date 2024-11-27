package inc.kaizen.service.education

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import inc.kaizen.service.sprout.annotation.Reference
import java.util.*

@Model(
    name = "Student",
    description = "Student model",
    basePackageName = "inc.kaizen.service.education",
    serviceName = "student",
    author  = "Kaizen",
    schema = "public",
    since = "2023-10-01",
    version = "1.0",
    api = StudentApi::class
)
data class Student(
    @Id
    val id: UUID,
    val name: String,
    @Reference
    val standard: Standard
)

@API(
    serviceName = "student",
    description = "Student API",
    basePackageName = "inc.kaizen.service.education",
    parents = ["school", "standard"],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = School::class
)
interface StudentApi {
    // Implementation of the service
}