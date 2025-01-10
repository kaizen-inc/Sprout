package inc.kaizen.service.tusker

import inc.kaizen.service.education.School
import inc.kaizen.service.education.StudentApi
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Defect",
    description = "Defect model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "defect",
    author  = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = DefectApi::class
)
data class Defect(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: DefectStatus
)

enum class DefectStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}

@API(
    serviceName = "defect",
    description = "Defect API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Defect::class
)
interface DefectApi
