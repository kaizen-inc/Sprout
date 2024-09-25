package inc.kaizen.service.sprout

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Example",
    description = "Example model",
    basePackageName = "inc.kaizen.service.sprout",
    serviceName = "example",
    author  = "Kaizen",
    since = "0.0.1",
    version = "0.0.1"
)
data class Example(
    val id: UUID,
//    @Field(
//        name = "name",
//        description = "Name",
//        since = "0.0.1",
//        version = "0.0.1"
//    )
    val name: String
)

@API(
    serviceName = "example",
    description = "This is an example API",
    basePackageName = "inc.kaizen.service.sprout",
    version = "1.0",
    author = "Author Name",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = Example::class
)
class ExampleApi {
    // Implementation of the service
}
