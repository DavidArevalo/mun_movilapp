package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.AppColourPalette;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppColourPalette} and its DTO {@link AppColourPaletteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppColourPaletteMapper extends EntityMapper<AppColourPaletteDTO, AppColourPalette> {
    @Mapping(target = "applications", source = "applications", qualifiedByName = "applicationIdSet")
    AppColourPaletteDTO toDto(AppColourPalette s);

    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "removeApplication", ignore = true)
    AppColourPalette toEntity(AppColourPaletteDTO appColourPaletteDTO);

    @Named("applicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationDTO toDtoApplicationId(Application application);

    @Named("applicationIdSet")
    default Set<ApplicationDTO> toDtoApplicationIdSet(Set<Application> application) {
        return application.stream().map(this::toDtoApplicationId).collect(Collectors.toSet());
    }
}
