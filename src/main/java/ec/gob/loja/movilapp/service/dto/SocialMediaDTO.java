package ec.gob.loja.movilapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.SocialMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialMediaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @NotNull(message = "must not be null")
    private String url;

    @NotNull(message = "must not be null")
    private String icon;

    private String colour;

    private Set<ApplicationDTO> applications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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
        if (!(o instanceof SocialMediaDTO)) {
            return false;
        }

        SocialMediaDTO socialMediaDTO = (SocialMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, socialMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialMediaDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", colour='" + getColour() + "'" +
            ", applications=" + getApplications() +
            "}";
    }
}
