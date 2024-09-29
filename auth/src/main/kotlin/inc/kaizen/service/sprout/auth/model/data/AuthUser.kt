package inc.kaizen.service.sprout.auth.model.data

import inc.kaizen.service.sprout.base.model.data.Base
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class AuthUser(
    @Id
    var name: String,
    var secret: String,
    var expiredEvent: Boolean,
    var locked: Boolean,
    var credentialsExpired: Boolean,
    var enabled: Boolean,
    var grantedAuthorities: Set<GrantedAuthority>
): UserDetails, Base() {
    override fun getAuthorities() = grantedAuthorities
    override fun getPassword() = secret
    override fun getUsername() = name
    override fun isAccountNonExpired() = !expiredEvent
    override fun isAccountNonLocked() = !locked
    override fun isCredentialsNonExpired() = !credentialsExpired
    override fun isEnabled() = enabled
}