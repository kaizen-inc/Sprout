package inc.kaizen.service.sprout.auth.converter

import inc.kaizen.service.sprout.auth.model.data.AuthUser
import inc.kaizen.service.sprout.auth.model.data.Authority
import inc.kaizen.service.sprout.auth.model.entity.AuthUserEntity
import inc.kaizen.service.sprout.base.configuration.MappingConfiguration
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority

@Mapper(componentModel = "spring", config = MappingConfiguration::class)
abstract class AuthUserConverter: Converter<AuthUserEntity, AuthUser> {

    abstract override fun convert(entity: AuthUserEntity): AuthUser

    fun map(value: String): GrantedAuthority = Authority("ROLE_$value")
}
