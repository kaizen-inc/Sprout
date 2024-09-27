package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class RepositoryClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val basePackaageName = extensions[BASE_PACKAGE_NAME]
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()

        appendLine("package $packageName")
        appendLine()
        appendLine("import org.springframework.data.jpa.repository.JpaRepository")
        appendLine("import org.springframework.stereotype.Repository")
        appendLine("import java.util.UUID")
        appendLine("import $basePackaageName.$serviceName.model.entity.${capitalizeServiceName}Entity")
        appendLine()
        appendLine("@Repository")
        appendLine("interface ${className}: JpaRepository<${capitalizeServiceName}Entity, UUID>")
    }

    override fun classNameSuffix() = "Repository"
}