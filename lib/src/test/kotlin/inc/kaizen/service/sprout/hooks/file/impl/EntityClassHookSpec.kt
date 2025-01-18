package inc.kaizen.service.sprout.hooks.file.impl

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.extension.findComplexType
import inc.kaizen.service.sprout.extension.findIdField
import inc.kaizen.service.sprout.extension.getProperties
import inc.kaizen.service.sprout.constant.*
import inc.kaizen.service.sprout.hooks.Component
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

class EntityClassHookSpec: FunSpec ({
    xtest("should hook the class") {
        val component = mockk<Component>()
        val model = mockk<KSClassDeclaration>()
        val id = mockk<KSPropertyDeclaration>()

        every { model.getAllProperties() } returns sequenceOf(id)
        every { model.findIdField() } returns id
        every { model.findComplexType() } returns emptySequence()
        every { model.getProperties(any()) } returns sequenceOf(id)

        val extensions = mapOf(
            PACKAGE_NAME to "inc.kaizen.service.sprout",
            BASE_PACKAGE_NAME to "inc.kaizen.service.sprout",
            SERVICE_NAME to "ServiceName",
            CLASS_NAME to "ClassName",
            MODEL_PACKAGE_NAME to "inc.kaizen.service.sprout",
            "schema" to "schema",
            "model" to model
        )
        val hook = EntityClassHook()
        val typeSpecBuilder = hook.hook(component, extensions)
        typeSpecBuilder.shouldNotBeNull()
        val typeSpec = typeSpecBuilder.build()
        typeSpec.name shouldBe "ClassName"
        typeSpec.toString() shouldBe """
            |package inc.kaizen.service.sprout
            |
            |import inc.kaizen.service.sprout.base.model.entity.BaseEntity
            |import inc.kaizen.service.sprout.model.entity.ServiceNameEntity
            |import java.util.*
            |import jakarta.persistence.*
            |
            |@Entity
            |@Table(name = "ServiceName", schema = "schema")
            |class ClassName(
            |): BaseEntity()
        """.trimMargin()
    }
})

data class Test(
    @Id
    val id: UUID,
    val name: String
)