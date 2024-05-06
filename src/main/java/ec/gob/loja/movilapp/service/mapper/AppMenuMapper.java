package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.AppMenu;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.service.dto.AppMenuDTO;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppMenu} and its DTO {@link AppMenuDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppMenuMapper extends EntityMapper<AppMenuDTO, AppMenu> {
    @Mapping(target = "application", source = "application", qualifiedByName = "applicationId")
    AppMenuDTO toDto(AppMenu s);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);
}
