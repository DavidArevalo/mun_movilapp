package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.domain.SocialMedia;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialMedia} and its DTO {@link SocialMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialMediaMapper extends EntityMapper<SocialMediaDTO, SocialMedia> {
    @Mapping(target = "applications", source = "applications", qualifiedByName = "applicationIdSet")
    SocialMediaDTO toDto(SocialMedia s);

    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "removeApplication", ignore = true)
    SocialMedia toEntity(SocialMediaDTO socialMediaDTO);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);

    @Named("applicationIdSet")
    default Set<ApplicationDTO> toDtoApplicationIdSet(Set<Application> application) {
        return application.stream().map(this::toDtoApplicationId).collect(Collectors.toSet());
    }
}
