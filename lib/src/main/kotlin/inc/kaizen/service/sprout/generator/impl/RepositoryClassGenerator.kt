package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.IClassContentGenerator

class RepositoryClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String) = buildString {
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.data.jpa.repository.JpaRepository")
        appendLine("import org.springframework.stereotype.Repository")
        appendLine("import java.util.UUID")
        appendLine()
        appendLine("@Repository")
        appendLine("interface ${className}: JpaRepository<${capitalizeServiceName}, UUID>")
    }

    override fun classNameSuffix() = "Repository"
}