package inc.kaizen.service.sprout.base.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

interface IController<T> {

    @Throws(Exception::class)
    fun create(t: T): ResponseEntity<Any>

    @Throws(Exception::class)
    fun deleteById(@PathVariable id: Array<out String>): ResponseEntity<Any>

    @Throws(Exception::class)
    fun update(t: T): ResponseEntity<Any>

    @Throws(Exception::class)
    fun findById(@PathVariable id: Array<out String>): ResponseEntity<Any>

    @Throws(Exception::class)
    fun findAll(
        page: Int,
        pageSize: Int
    ): ResponseEntity<Any>
}

