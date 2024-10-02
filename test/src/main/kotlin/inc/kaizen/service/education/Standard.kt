package inc.kaizen.service.education

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import inc.kaizen.service.sprout.annotation.Reference
import java.util.*

@Model(
    name = "Standard",
    description = "Standard model",
    basePackageName = "inc.kaizen.service.education",
    serviceName = "standard",
    author  = "Kaizen",
    since = "2023-10-01",
    schema = "public",
    version = "1.0"
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
    version = "1.0",
    author = "Kaizen",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = Standard::class
)
class StandardApi {
    // Implementation of the service
}