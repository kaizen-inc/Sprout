package inc.kaizen.service.sprout.hooks.file.impl

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import inc.kaizen.service.sprout.base.model.entity.BaseEntity
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.extension.findIdField
import inc.kaizen.service.sprout.extension.getProperties
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.util.*

class EntityClassHook: IClassHook {
    override fun hook(
        component: Component,
        extensions: Map<String, Any>
    ): TypeSpec.Builder {
        val serviceName = extensions[SERVICE_NAME] as String
        val className = extensions[CLASS_NAME] as String
        val schema = extensions["schema"] as String

        val modelClass = extensions["model"] as KSClassDeclaration
        val complexFields = modelClass.findComplexType()


        val typeSpecBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(Entity::class)
            .addAnnotation(AnnotationSpec.builder(Table::class)
                .addMember("name = %S", serviceName)
                .addMember("schema = %S", schema)
                .build())
            .superclass(BaseEntity::class)

        val constructorBuilder = FunSpec.constructorBuilder()
        modelClass.findIdField().let { field ->
            constructorBuilder.addParameter(ParameterSpec.builder("id",
                UUID::class.asClassName())
                .addAnnotation(Id::class)
                .apply {
                    if (field.type.toString() == "UUID") {
                        addAnnotation(AnnotationSpec.builder(GeneratedValue::class)
                            .addMember("strategy = %T.AUTO", GenerationType::class)
                            .build())
                    }
                }
                .build())
            typeSpecBuilder.addProperty(PropertySpec
                .builder("id", UUID::class.asClassName())
                .initializer("id")
                .build())
        }

        modelClass.getProperties(false).forEach { it ->
            val declaration = it.type.resolve().declaration
            var propertySpecBuilder = ParameterSpec.builder(it.toString(),
                ClassName(declaration.packageName.asString(), declaration.simpleName.asString()))
            if (complexFields.contains(it)) {
                propertySpecBuilder.addAnnotation(ManyToOne::class)
                propertySpecBuilder.addAnnotation(AnnotationSpec.builder(JoinColumn::class)
                    .addMember("name = %S", it.type.toString().toCamelCase())
                    .build())
            } else {
                if (declaration is KSClassDeclaration && declaration.classKind == ClassKind.ENUM_CLASS) {
                    propertySpecBuilder.addAnnotation(AnnotationSpec.builder(Enumerated::class)
                        .addMember("value = %T.STRING", EnumType::class)
                        .build())
                } else if (it.type.resolve().declaration.qualifiedName?.asString() == "java.util.Date") {
                    propertySpecBuilder.addAnnotation(AnnotationSpec.builder(Temporal::class)
                        .addMember("value = %T.TIMESTAMP", TemporalType::class)
                        .build())
                }
            }
            constructorBuilder.addParameter(propertySpecBuilder.build())
            typeSpecBuilder.addProperty(PropertySpec
                .builder(it.toString(), ClassName(declaration.packageName.asString(), declaration.simpleName.asString()))
                .initializer(it.toString())
                .build())
        }

        typeSpecBuilder.primaryConstructor(constructorBuilder.build())
        return typeSpecBuilder
    }
}