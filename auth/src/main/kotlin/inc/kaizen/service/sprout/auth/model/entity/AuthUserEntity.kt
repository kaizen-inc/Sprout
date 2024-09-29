package inc.kaizen.service.sprout.auth.model.entity

import inc.kaizen.service.sprout.base.model.entity.BaseEntity
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user", schema = "public")
data class AuthUserEntity(
    @Id
    var name: String,
    var secret: String,
    var expiredEvent: Boolean,
    var locked: Boolean,
    var credentialsExpired: Boolean,
    var enabled: Boolean,
    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER)
    var grantedAuthorities: Set<AuthorityEntity?>? = null
): UserDetails, BaseEntity() {
    override fun getAuthorities() = grantedAuthorities
    override fun getPassword() = secret
    override fun getUsername() = name
    override fun isAccountNonExpired() = !expiredEvent
    override fun isAccountNonLocked() = !locked
    override fun isCredentialsNonExpired() = !credentialsExpired
    override fun isEnabled() = enabled
}