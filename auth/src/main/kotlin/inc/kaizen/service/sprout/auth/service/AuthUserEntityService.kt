package inc.kaizen.service.sprout.auth.service

import inc.kaizen.service.sprout.auth.model.data.AuthUser
import inc.kaizen.service.sprout.auth.model.entity.AuthUserEntity
import inc.kaizen.service.sprout.base.extension.nonNullify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.expression.ParseException
import org.springframework.stereotype.Service

@Service
class AuthUserEntityService {

    @Autowired
    private lateinit var conversionService: ConversionService

    @Throws(ParseException::class)
    fun convert(school: AuthUser): AuthUserEntity {
       val schoolEntity: AuthUserEntity = conversionService
           .convert(school, AuthUserEntity::class.java)
           .nonNullify()
       return schoolEntity
    }

    @Throws(ParseException::class)
    fun convert(entity: AuthUserEntity): AuthUser {
        val school: AuthUser = conversionService
            .convert(entity, AuthUser::class.java)
            .nonNullify()
        return school
    }
}
