package inc.kaizen.service.sprout.base.model.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    lateinit var created: Date

    @LastModifiedDate
    @Column(name = "modified", nullable = false)
    lateinit var modified: Date
}
