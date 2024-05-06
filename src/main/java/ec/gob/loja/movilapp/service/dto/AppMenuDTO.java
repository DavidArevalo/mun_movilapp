package ec.gob.loja.movilapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.AppMenu} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMenuDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @NotNull(message = "must not be null")
    private String url;

    @NotNull(message = "must not be null")
    private String icon;

    private Boolean isActive;

    private Integer priority;

    private String component;

    private ApplicationDTO application;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppMenuDTO)) {
            return false;
        }

        AppMenuDTO appMenuDTO = (AppMenuDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appMenuDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppMenuDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", priority=" + getPriority() +
            ", component='" + getComponent() + "'" +
            ", application=" + getApplication() +
            "}";
    }
}
