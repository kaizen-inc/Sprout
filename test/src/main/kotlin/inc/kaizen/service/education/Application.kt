package inc.kaizen.service.education

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan(
    "inc.kaizen.service.sprout.base",
    "inc.kaizen.service.sprout.auth",
    "inc.kaizen.service.education"
)
@EnableJpaRepositories(
    "inc.kaizen.service.sprout.auth.repository",
    "inc.kaizen.service.sprout.base.repository",
    "inc.kaizen.service.education.school.repository",
    "inc.kaizen.service.education.student.repository",
    "inc.kaizen.service.education.standard.repository"
)
@EntityScan(basePackages = [
    "inc.kaizen.service.sprout.auth.model.entity",
    "inc.kaizen.service.sprout.base.model.entity",
    "inc.kaizen.service.education.school.model.entity",
    "inc.kaizen.service.education.student.model.entity",
    "inc.kaizen.service.education.standard.model.entity"
])
@SpringBootApplication
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java)
}