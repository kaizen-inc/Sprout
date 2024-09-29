package inc.kaizen.service.sprout.base.configuration

import inc.kaizen.service.sprout.base.model.data.Base
import inc.kaizen.service.sprout.base.model.entity.BaseEntity
import org.mapstruct.InjectionStrategy
import org.mapstruct.MapperConfig
import org.mapstruct.Mapping
import org.mapstruct.MappingInheritanceStrategy
import org.mapstruct.extensions.spring.SpringMapperConfig

@MapperConfig(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
@SpringMapperConfig
interface MappingConfiguration {
    @Mapping(target = "modified")
    @Mapping(target = "created")
    fun baseToEntity(base: Base): BaseEntity
}