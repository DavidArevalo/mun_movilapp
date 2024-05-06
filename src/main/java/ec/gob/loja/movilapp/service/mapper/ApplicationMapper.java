package ec.gob.loja.movilapp.service.mapper;

import ec.gob.loja.movilapp.domain.AppBanner;
import ec.gob.loja.movilapp.domain.AppColourPalette;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.domain.SocialMedia;
import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Application} and its DTO {@link ApplicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationMapper extends EntityMapper<ApplicationDTO, Application> {
    @Mapping(target = "banners", source = "banners", qualifiedByName = "appBannerIdSet")
    @Mapping(target = "colourPalettes", source = "colourPalettes", qualifiedByName = "appColourPaletteIdSet")
    @Mapping(target = "socialMedias", source = "socialMedias", qualifiedByName = "socialMediaIdSet")
    ApplicationDTO toDto(Application s);

    @Mapping(target = "removeBanner", ignore = true)
    @Mapping(target = "removeColourPalette", ignore = true)
    @Mapping(target = "removeSocialMedia", ignore = true)
    Application toEntity(ApplicationDTO applicationDTO);

    @Named("appBannerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppBannerDTO toDtoAppBannerId(AppBanner appBanner);

    @Named("appBannerIdSet")
    default Set<AppBannerDTO> toDtoAppBannerIdSet(Set<AppBanner> appBanner) {
        return appBanner.stream().map(this::toDtoAppBannerId).collect(Collectors.toSet());
    }

    @Named("appColourPaletteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppColourPaletteDTO toDtoAppColourPaletteId(AppColourPalette appColourPalette);

    @Named("appColourPaletteIdSet")
    default Set<AppColourPaletteDTO> toDtoAppColourPaletteIdSet(Set<AppColourPalette> appColourPalette) {
        return appColourPalette.stream().map(this::toDtoAppColourPaletteId).collect(Collectors.toSet());
    }

    @Named("socialMediaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SocialMediaDTO toDtoSocialMediaId(SocialMedia socialMedia);

    @Named("socialMediaIdSet")
    default Set<SocialMediaDTO> toDtoSocialMediaIdSet(Set<SocialMedia> socialMedia) {
        return socialMedia.stream().map(this::toDtoSocialMediaId).collect(Collectors.toSet());
    }
}
