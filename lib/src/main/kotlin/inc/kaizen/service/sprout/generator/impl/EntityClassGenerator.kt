package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.IClassContentGenerator

class EntityClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String) = buildString {
        val capitalizeServiceName = serviceName.capitalizeFirstLetter()
        appendLine("package $packageName")
        appendLine()
        appendLine("import java.util.*")
        appendLine()
        appendLine("@Entity")
        appendLine("@Table(name = \"$serviceName\"), schema = \"public\")")
        appendLine("data class $className(")
        appendLine("    @Id")
        appendLine("    @GeneratedValue(strategy = GenerationType.AUTO)")
        appendLine("    val id: UUID")
        appendLine("): BaseEntity()")
    }

    override fun classNameSuffix() = "Entity"
}