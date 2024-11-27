package inc.kaizen.service.sprout.base.service

import org.springframework.data.domain.Page

interface IService<T, U> {

    companion object {
        const val DEFAULT_PAGE_SIZE = "10"
        const val DEFAULT_PAGE_NUMBER = "0"
    }

    @Throws(Exception::class)
    fun create(t: T): T

    @Throws(Exception::class)
    fun deleteById(id: Array<out U>)

    @Throws(Exception::class)
    fun update(t: T): T

    @Throws(Exception::class)
    fun findById(id: Array<out U>): T?

    @Throws(Exception::class)
    fun findAll(
        page: Int,
        pageSize: Int
    ): Page<T>
}

