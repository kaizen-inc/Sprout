package inc.kaizen.service.sprout

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "School",
    description = "School model",
    basePackageName = "inc.kaizen.service.education",
    serviceName = "school",
    author  = "Kaizen",
    since = "2023-10-01",
    version = "1.0"
)
data class School(
    val id: UUID,
    val name: String
)

@API(
    serviceName = "school",
    description = "School API",
    basePackageName = "inc.kaizen.service.education",
    version = "1.0",
    author = "Kaizen",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = School::class
)
class SchoolApi {
    // Implementation of the service
}