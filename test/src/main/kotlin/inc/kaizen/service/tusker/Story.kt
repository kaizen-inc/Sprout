package inc.kaizen.service.tusker

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Story",
    description = "Story model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "story",
    author  = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = StoryApi::class
)
data class Story(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: StoryStatus
)

enum class StoryStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}


@API(
    serviceName = "story",
    description = "Story API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Story::class
)
interface StoryApi