package inc.kaizen.service.sprout.base.extension

import org.springframework.http.ResponseEntity
import java.util.*

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

fun <T> closureWithReturn(data: () -> T): ResponseEntity<Any> {
	return try {
		ResponseEntity.ok(data())
	} catch (e: Exception) {
		ResponseEntity.internalServerError().body(e.message)
	}
}

fun closureWithoutReturn(data: () -> Unit): ResponseEntity<Any> {
	return try {
		data()
		ResponseEntity.ok().build()
	} catch (e: Exception) {
		ResponseEntity.badRequest().body(e.message)
	}
}

fun String.toUUID(): UUID {
	return UUID.fromString(this)
}

