package inc.kaizen.service.tusker

import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Project",
    description = "Project model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "project",
    author = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = ProjectApi::class
)
data class Project(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: ProjectStatus
)

enum class ProjectStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}

@API(
    serviceName = "project",
    description = "Project API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Project::class
)
interface ProjectApi
