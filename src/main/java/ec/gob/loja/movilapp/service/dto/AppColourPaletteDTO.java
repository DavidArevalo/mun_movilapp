package ec.gob.loja.movilapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.AppColourPalette} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppColourPaletteDTO implements Serializable {

    private Long id;

    private String description;

    private String primaryColour;

    private String secondaryColour;

    private String tertiaryColour;

    private String neutralColour;

    private String ligthBackgroundColour;

    private String darkBackgroundColour;

    private Set<ApplicationDTO> applications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
    }

    public String getSecondaryColour() {
        return secondaryColour;
    }

    public void setSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public String getTertiaryColour() {
        return tertiaryColour;
    }

    public void setTertiaryColour(String tertiaryColour) {
        this.tertiaryColour = tertiaryColour;
    }

    public String getNeutralColour() {
        return neutralColour;
    }

    public void setNeutralColour(String neutralColour) {
        this.neutralColour = neutralColour;
    }

    public String getLigthBackgroundColour() {
        return ligthBackgroundColour;
    }

    public void setLigthBackgroundColour(String ligthBackgroundColour) {
        this.ligthBackgroundColour = ligthBackgroundColour;
    }

    public String getDarkBackgroundColour() {
        return darkBackgroundColour;
    }

    public void setDarkBackgroundColour(String darkBackgroundColour) {
        this.darkBackgroundColour = darkBackgroundColour;
    }

    public Set<ApplicationDTO> getApplications() {
        return applications;
    }

    public void setApplications(Set<ApplicationDTO> applications) {
        this.applications = applications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppColourPaletteDTO)) {
            return false;
        }

        AppColourPaletteDTO appColourPaletteDTO = (AppColourPaletteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appColourPaletteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppColourPaletteDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", primaryColour='" + getPrimaryColour() + "'" +
            ", secondaryColour='" + getSecondaryColour() + "'" +
            ", tertiaryColour='" + getTertiaryColour() + "'" +
            ", neutralColour='" + getNeutralColour() + "'" +
            ", ligthBackgroundColour='" + getLigthBackgroundColour() + "'" +
            ", darkBackgroundColour='" + getDarkBackgroundColour() + "'" +
            ", applications=" + getApplications() +
            "}";
    }
}
