package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.AppBanner;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppBanner} and its DTO {@link AppBannerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppBannerMapper extends EntityMapper<AppBannerDTO, AppBanner> {
    @Mapping(target = "applications", source = "applications", qualifiedByName = "applicationIdSet")
    AppBannerDTO toDto(AppBanner s);

    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "removeApplication", ignore = true)
    AppBanner toEntity(AppBannerDTO appBannerDTO);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);

    @Named("applicationIdSet")
    default Set<ApplicationDTO> toDtoApplicationIdSet(Set<Application> application) {
        return application.stream().map(this::toDtoApplicationId).collect(Collectors.toSet());
    }
}
