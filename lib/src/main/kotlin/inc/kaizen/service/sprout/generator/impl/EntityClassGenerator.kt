package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.*

class EntityClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackageName = extensions[BASE_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]

        appendLine("package $packageName")
        appendLine()
        appendLine("import java.util.*")
        appendLine("import jakarta.persistence.*")
        appendLine("import $basePackageName.base.model.entity.BaseEntity")
        appendLine()
        appendLine("@Entity")
        appendLine("@Table(name = \"$serviceName\", schema = \"public\")")
        appendLine("data class $className(")
        appendLine("    @Id")
        appendLine("    @GeneratedValue(strategy = GenerationType.AUTO)")
        appendLine("    val id: UUID")
        appendLine("): BaseEntity()")
    }

    override fun classNameSuffix() = "Entity"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.$serviceName.model.entity"
}