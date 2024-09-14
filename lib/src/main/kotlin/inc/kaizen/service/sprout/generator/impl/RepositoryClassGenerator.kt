package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator

class RepositoryClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, serviceName: String): String {
        return buildString {
            appendLine("package $packageName.$serviceName")
            appendLine()
            appendLine("import org.springframework.data.jpa.repository.JpaRepository")
            appendLine("import org.springframework.stereotype.Repository")
            appendLine("import java.util.UUID")
            appendLine()
            appendLine("@Repository")
            appendLine("interface ${serviceName}Repository: JpaRepository<${serviceName}, UUID>")
        }
    }

    override fun classNameSuffix() = "Repository"
}