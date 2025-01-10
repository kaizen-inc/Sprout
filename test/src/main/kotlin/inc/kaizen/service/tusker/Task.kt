package inc.kaizen.service.tusker

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Task",
    description = "Task model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "task",
    author  = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = TaskApi::class
)
data class Task(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: TaskStatus
)

enum class TaskStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}

@API(
    serviceName = "task",
    description = "Task API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Task::class
)
interface TaskApi
