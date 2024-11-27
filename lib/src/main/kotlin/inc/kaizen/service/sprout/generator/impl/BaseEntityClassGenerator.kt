package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.PACKAGE_NAME

@Deprecated("This generator class is not used anymore")
class BaseEntityClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME] as String

        appendLine("package $packageName")
        appendLine()
        appendLine("import jakarta.persistence.Column")
        appendLine("import jakarta.persistence.EntityListeners")
        appendLine("import jakarta.persistence.MappedSuperclass")
        appendLine("import org.springframework.data.annotation.CreatedDate")
        appendLine("import org.springframework.data.annotation.LastModifiedDate")
        appendLine("import org.springframework.data.jpa.domain.support.AuditingEntityListener")
        appendLine("import java.util.*")
        appendLine()
        appendLine("@MappedSuperclass")
        appendLine("@EntityListeners(AuditingEntityListener::class)")
        appendLine("abstract class BaseEntity {")
        appendLine()
        appendLine("    @CreatedDate")
        appendLine("    @Column(name = \"created_at\", nullable = false, updatable = false)")
        appendLine("    lateinit var createdAt: Date")
        appendLine()
        appendLine("    @LastModifiedDate")
        appendLine("    @Column(name = \"updated_at\", nullable = false)")
        appendLine("    lateinit var updatedAt: Date")
        appendLine("}")
    }

    override fun classNameSuffix() = ""

    override fun packageNameSuffix() = "base"

    override fun className(serviceName: String) = "BaseEntity"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.base.model.entity"
}