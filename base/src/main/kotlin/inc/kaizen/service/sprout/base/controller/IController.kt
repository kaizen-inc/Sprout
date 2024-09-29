package inc.kaizen.service.sprout.base.controller

import org.springframework.http.ResponseEntity

interface IController<T> {

    @Throws(Exception::class)
    fun create(t: T): ResponseEntity<Any>

    @Throws(Exception::class)
    fun deleteById(id: String): ResponseEntity<Any>

    @Throws(Exception::class)
    fun update(t: T): ResponseEntity<Any>

    @Throws(Exception::class)
    fun findById(id: String): ResponseEntity<Any>

    @Throws(Exception::class)
    fun findAll(
        page: Int,
        pageSize: Int
    ): ResponseEntity<Any>
}

