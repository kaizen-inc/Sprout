package inc.kaizen.service.sprout.generator.impl

import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.generator.IClassContentGenerator

class BaseEntityClassGenerator: IClassContentGenerator {

    override fun generateContent(packageName: String, className: String, serviceName: String) = buildString {
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
        appendLine("data class BaseEntity(")
        appendLine()
        appendLine("    @CreatedDate")
        appendLine("    @Column(name = \"created_at\", nullable = false, updatable = false)")
        appendLine("    val createdAt: Date,")
        appendLine()
        appendLine("    @LastModifiedDate")
        appendLine("    @Column(name = \"updated_at\", nullable = false)")
        appendLine("    val updatedAt: Date")
        appendLine(")")
    }

    override fun classNameSuffix() = "Entity"
}