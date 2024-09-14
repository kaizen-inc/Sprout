package inc.kaizen.service.sprout.extension

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueArgument

fun <T, U> T?.get(block: (T) -> U): U {
    if(this != null) return block(this) else throw IllegalArgumentException("object is null")
}

fun <T> T?.otherwise(block: () -> Unit) {
    if (this == null) block()
}

fun <T, U> T?.getOrOtherwise(block: (T) -> U, otherwise: () -> U): U {
    if (this != null) return block(this) else return otherwise()
}

fun  <T> T?.nonNullify(): T {
    if(this != null) return this else throw IllegalArgumentException("object is null")
}

fun KSAnnotation.findArgumentByName(argument: String) = arguments.find { it.name?.asString() == argument }

fun KSAnnotation.findArgument(argument: KSValueArgument) = arguments.find { it == argument }