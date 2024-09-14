package inc.kaizen.service.sprout

import inc.kaizen.service.sprout.annotation.API

@API(
    serviceName = "Example",
    description = "This is an example API",
    packageName = "inc.kaizen.service.sprout",
    version = "1.0",
    author = "Author Name",
    since = "2023-10-01",
    deprecated = false,
    authentication = true,
    model = Example::class
)
class ExampleService {
    // Implementation of the service
}