package inc.kaizen.service.sprout.auth.converter

import inc.kaizen.service.sprout.auth.model.data.AuthUser
import inc.kaizen.service.sprout.auth.model.entity.AuthUserEntity
import inc.kaizen.service.sprout.base.configuration.MappingConfiguration
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper(componentModel = "spring", config = MappingConfiguration::class)
abstract class AuthUserEntityConverter: Converter<AuthUser, AuthUserEntity> {

    abstract override fun convert(entity: AuthUser): AuthUserEntity
}
