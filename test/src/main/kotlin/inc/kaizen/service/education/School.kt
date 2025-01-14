package inc.kaizen.service.education

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "School",
    description = "School model",
    basePackageName = "inc.kaizen.service.education",
    serviceName = "school",
    author  = "Kaizen",
    since = "2023-10-01",
    schema = "public",
    version = "1.0",
    api = SchoolApi::class
)
data class School(
    @Id
    val id: UUID,
    val name: String
)

@API(
    serviceName = "school",
    description = "School API",
    basePackageName = "inc.kaizen.service.education",
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = School::class
)
interface SchoolApi {
    // Implementation of the service
}