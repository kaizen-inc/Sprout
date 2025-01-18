package inc.kaizen.service.tusker

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Milestone",
    description = "Milestone model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "milestone",
    author = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = MilestoneApi::class
)
data class Milestone(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: MilestoneStatus
)

enum class MilestoneStatus {
    NOT_STARTED,
    IN_PROGRESS,
    DONE,
}

@API(
    serviceName = "milestone",
    description = "Milestone API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Milestone::class
)
interface MilestoneApi
