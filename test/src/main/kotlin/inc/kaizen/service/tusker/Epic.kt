package inc.kaizen.service.tusker

import inc.kaizen.service.education.StudentApi
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Epic",
    description = "Epic model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "epic",
    author  = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = EpicApi::class
)
data class Epic(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: EpicStatus
)

enum class EpicStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}

@API(
    serviceName = "epic",
    description = "Epic API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Epic::class
)
interface EpicApi