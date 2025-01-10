package inc.kaizen.service.tusker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan(
    "inc.kaizen.service.sprout.base",
    "inc.kaizen.service.sprout.auth",
    "inc.kaizen.service.tusker"
)
@EntityScan(basePackages = [
    "inc.kaizen.service.sprout.auth.model.entity",
    "inc.kaizen.service.sprout.base.model.entity",
    "inc.kaizen.service.tusker.task.model.entity",
    "inc.kaizen.service.tusker.story.model.entity",
    "inc.kaizen.service.tusker.feature.model.entity",
    "inc.kaizen.service.tusker.project.model.entity",
    "inc.kaizen.service.tusker.defect.model.entity",
    "inc.kaizen.service.tusker.epic.model.entity",
])
@EnableJpaRepositories(
    "inc.kaizen.service.sprout.auth.repository",
    "inc.kaizen.service.sprout.base.repository",
    "inc.kaizen.service.tusker.task.repository",
    "inc.kaizen.service.tusker.story.repository",
    "inc.kaizen.service.tusker.feature.repository",
    "inc.kaizen.service.tusker.project.repository",
    "inc.kaizen.service.tusker.defect.repository",
    "inc.kaizen.service.tusker.epic.repository"
)
@SpringBootApplication
open class Tusker

fun main(args: Array<String>) {
    SpringApplication.run(Tusker::class.java)
}