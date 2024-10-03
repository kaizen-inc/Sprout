package inc.kaizen.service.sprout.generator.impl

import com.google.devtools.ksp.symbol.KSClassDeclaration
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.extension.findIdField
import inc.kaizen.service.sprout.extension.getProperties
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.*

class EntityClassGenerator: IClassContentGenerator {

    override fun generateContent(extensions: Map<String, Any>) = buildString {
        val packageName = extensions[PACKAGE_NAME]
        val basePackageName = extensions[BASE_PACKAGE_NAME]
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME]
        val modelPackageName = extensions[MODEL_PACKAGE_NAME]
        val schema = extensions["schema"]

        val modelClass = extensions["model"] as KSClassDeclaration
        val complexFields = modelClass.findComplexType()

        appendLine("package $packageName")
        appendLine()
        appendLine("import java.util.*")
        appendLine("import jakarta.persistence.*")
        appendLine("import inc.kaizen.service.sprout.base.model.entity.BaseEntity")
        complexFields.iterator().forEach {
            appendLine("import $basePackageName.${it.type.toString().toCamelCase()}.model.entity.${it.type}Entity")
        }
        appendLine()
        appendLine("@Entity")
        appendLine("@Table(name = \"$serviceName\", schema = \"$schema\")")
        appendLine("data class $className(")
//        appendLine("    @GeneratedValue(strategy = GenerationType.AUTO)")
        modelClass.findIdField().let {
            appendLine("    @Id")
            if (it.type.toString() == "UUID") {
                appendLine("    @GeneratedValue(strategy = GenerationType.AUTO)")
            }
            appendLine("    val ${it}: ${it.type},")
        }
        modelClass.getProperties(false).forEach { it ->
            if(complexFields.contains(it)) {
                appendLine("    @ManyToOne") //FIXME: find the relevant mapping
                appendLine("    @JoinColumn(name = \"${it.type.toString().toCamelCase()}\")")
                appendLine("    val ${it}: ${it.type}Entity,")
            } else {
                appendLine("    val ${it}: ${it.type},")
            }
        }
        appendLine("): BaseEntity()")
    }

    override fun classNameSuffix() = "Entity"

    override fun packageName(basePackageName: String, serviceName: String) = "$basePackageName.$serviceName.model.entity"
}