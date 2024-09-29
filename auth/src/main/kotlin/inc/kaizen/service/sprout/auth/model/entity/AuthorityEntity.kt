package inc.kaizen.service.sprout.auth.model.entity

import inc.kaizen.service.sprout.base.model.entity.BaseEntity
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "authority", schema = "public")
data class AuthorityEntity(
    @Id
    var grantedAuthority: String,

    @ManyToOne
    @JoinColumn(name = "auth_user_detail_id")
    var authUser: AuthUserEntity
): GrantedAuthority, BaseEntity() {
    override fun getAuthority() = grantedAuthority
}