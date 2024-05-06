package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.AppServices;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.service.dto.AppServicesDTO;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppServices} and its DTO {@link AppServicesDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppServicesMapper extends EntityMapper<AppServicesDTO, AppServices> {
    @Mapping(target = "application", source = "application", qualifiedByName = "applicationId")
    AppServicesDTO toDto(AppServices s);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);
}
