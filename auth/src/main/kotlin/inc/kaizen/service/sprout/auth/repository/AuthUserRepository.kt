package inc.kaizen.service.sprout.auth.repository

import inc.kaizen.service.sprout.auth.model.entity.AuthUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthUserRepository: JpaRepository<AuthUserEntity, String> {
    fun findByUsername(username: String?): Optional<AuthUserEntity>
}