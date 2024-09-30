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
    schema = "education",
    since = "2023-10-01",
    version = "1.0"
)
data class Student(
    @Id
    val id: UUID,
    val name: String,
    @Reference
    val school: School
)

@API(
    serviceName = "student",
    description = "Student API",
    basePackageName = "inc.kaizen.service.education",
    version = "1.0",
    author = "Kaizen",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = School::class
)
class StudentApi {
    // Implementation of the service
}