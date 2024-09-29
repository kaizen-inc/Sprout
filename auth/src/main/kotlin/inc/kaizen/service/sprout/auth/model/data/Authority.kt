package inc.kaizen.service.sprout.auth.model.data

import inc.kaizen.service.sprout.base.model.data.Base
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority

data class Authority(
    @Id
    var grantedAuthority: String
): GrantedAuthority, Base() {
    override fun getAuthority() = grantedAuthority
}