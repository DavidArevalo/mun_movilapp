package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AppMenu.
 */
@Table("app_menu")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Column("url")
    private String url;

    @NotNull(message = "must not be null")
    @Column("icon")
    private String icon;

    @Column("is_active")
    private Boolean isActive;

    @Column("priority")
    private Integer priority;

    @Column("component")
    private String component;

    @Transient
    @JsonIgnoreProperties(
        value = { "services", "menus", "frequentlyQuestions", "banners", "colourPalettes", "socialMedias" },
        allowSetters = true
    )
    private Application application;

    @Column("application_id")
    private Long applicationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AppMenu title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public AppMenu url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public AppMenu icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppMenu isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public AppMenu priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getComponent() {
        return this.component;
    }

    public AppMenu component(String component) {
        this.setComponent(component);
        return this;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
        this.applicationId = application != null ? application.getId() : null;
    }

    public AppMenu application(Application application) {
        this.setApplication(application);
        return this;
    }

    public Long getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(Long application) {
        this.applicationId = application;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppMenu)) {
            return false;
        }
        return getId() != null && getId().equals(((AppMenu) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppMenu{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", priority=" + getPriority() +
            ", component='" + getComponent() + "'" +
            "}";
    }
}
