package ec.gob.loja.movilapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.Application} entity.
 */
@Schema(description = "START MOVILAPP")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String code;

    @NotNull(message = "must not be null")
    private String name;

    private String urlAndroid;

    private String urlIos;

    private String description;

    private Double version;

    private Boolean isActive;

    private Set<AppBannerDTO> banners = new HashSet<>();

    private Set<AppColourPaletteDTO> colourPalettes = new HashSet<>();

    private Set<SocialMediaDTO> socialMedias = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAndroid() {
        return urlAndroid;
    }

    public void setUrlAndroid(String urlAndroid) {
        this.urlAndroid = urlAndroid;
    }

    public String getUrlIos() {
        return urlIos;
    }

    public void setUrlIos(String urlIos) {
        this.urlIos = urlIos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AppBannerDTO> getBanners() {
        return banners;
    }

    public void setBanners(Set<AppBannerDTO> banners) {
        this.banners = banners;
    }

    public Set<AppColourPaletteDTO> getColourPalettes() {
        return colourPalettes;
    }

    public void setColourPalettes(Set<AppColourPaletteDTO> colourPalettes) {
        this.colourPalettes = colourPalettes;
    }

    public Set<SocialMediaDTO> getSocialMedias() {
        return socialMedias;
    }

    public void setSocialMedias(Set<SocialMediaDTO> socialMedias) {
        this.socialMedias = socialMedias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationDTO)) {
            return false;
        }

        ApplicationDTO applicationDTO = (ApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", urlAndroid='" + getUrlAndroid() + "'" +
            ", urlIos='" + getUrlIos() + "'" +
            ", description='" + getDescription() + "'" +
            ", version=" + getVersion() +
            ", isActive='" + getIsActive() + "'" +
            ", banners=" + getBanners() +
            ", colourPalettes=" + getColourPalettes() +
            ", socialMedias=" + getSocialMedias() +
            "}";
    }
}
