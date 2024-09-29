package inc.kaizen.service.sprout.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import inc.kaizen.service.sprout.annotation.Id
import inc.kaizen.service.sprout.annotation.Reference
import org.apache.commons.text.CaseUtils


fun KSAnnotation.findArgumentByName(argument: String) = arguments.find { it.name?.asString() == argument }

fun KSAnnotation.findArgument(argument: KSValueArgument) = arguments.find { it == argument }

fun String.toCamelCase(): String {
    return CaseUtils.toCamelCase(this, false)
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { it.uppercase() }
}

fun KSClassDeclaration.findComplexType(): Sequence<KSPropertyDeclaration> {
    val references = getAllProperties()
        .filter { member ->
            member.annotations.any { it.shortName.asString() == Reference::class.simpleName }
        }
    return references
}

fun KSClassDeclaration.findIdField(): KSPropertyDeclaration {
    val references = getAllProperties()
        .filter { member ->
            member.annotations.any { it.shortName.asString() == Id::class.simpleName }
        }
    return references.first()
}

fun KSClassDeclaration.getProperties(includeId: Boolean = true): Sequence<KSPropertyDeclaration> {
    val references = getAllProperties()
    if(includeId) {
        return references
    } else {
        return references.filter { member ->
            !member.annotations.any { it.shortName.asString() == Id::class.simpleName }
        }
    }
}