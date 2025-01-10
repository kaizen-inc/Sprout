package inc.kaizen.service.tusker

import inc.kaizen.service.education.StudentApi
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Model
import java.util.*

@Model(
    name = "Feature",
    description = "Feature model",
    basePackageName = "inc.kaizen.service.tusker",
    serviceName = "feature",
    author  = "Kaizen",
    schema = "public",
    since = "2023-12-14",
    version = "1.0",
    api = FeatureApi::class
)
data class Feature(
    @Id val id: UUID,
    val title: String,
    val description: String,
    val createdDate: Date,
    val lastModifiedDate: Date,
    val dueDate: Date? = null,
    val status: FeatureStatus
)

enum class FeatureStatus {
    OPEN,
    IN_PROGRESS,
    DONE,
    CANCELLED,
    ARCHIVED
}

@API(
    serviceName = "feature",
    description = "Feature API",
    basePackageName = "inc.kaizen.service.tusker",
    parents = [ ],
    version = "1.0",
    author = "Kaizen, Inc",
    since = "2023-12-14",
    deprecated = false,
    authentication = true,
    model = Feature::class
)
interface FeatureApi