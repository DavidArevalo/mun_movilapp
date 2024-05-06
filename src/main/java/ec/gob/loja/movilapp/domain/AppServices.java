package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AppServices.
 */
@Table("app_services")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppServices implements Serializable {

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

    @Column("background_card")
    private String backgroundCard;

    @Column("is_active")
    private Boolean isActive;

    @Column("priority")
    private Integer priority;

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

    public AppServices id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AppServices title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public AppServices url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public AppServices icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackgroundCard() {
        return this.backgroundCard;
    }

    public AppServices backgroundCard(String backgroundCard) {
        this.setBackgroundCard(backgroundCard);
        return this;
    }

    public void setBackgroundCard(String backgroundCard) {
        this.backgroundCard = backgroundCard;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppServices isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public AppServices priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
        this.applicationId = application != null ? application.getId() : null;
    }

    public AppServices application(Application application) {
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
        if (!(o instanceof AppServices)) {
            return false;
        }
        return getId() != null && getId().equals(((AppServices) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppServices{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", backgroundCard='" + getBackgroundCard() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
