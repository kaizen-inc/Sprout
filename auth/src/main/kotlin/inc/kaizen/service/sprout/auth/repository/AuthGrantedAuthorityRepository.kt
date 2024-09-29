package inc.kaizen.service.sprout.auth.repository

import inc.kaizen.service.sprout.auth.model.entity.AuthorityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthGrantedAuthorityRepository: JpaRepository<AuthorityEntity, String>