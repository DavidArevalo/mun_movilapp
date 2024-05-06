package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SocialMedia.
 */
@Table("social_media")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialMedia implements Serializable {

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

    @Column("colour")
    private String colour;

    @Transient
    @JsonIgnoreProperties(
        value = { "services", "menus", "frequentlyQuestions", "banners", "colourPalettes", "socialMedias" },
        allowSetters = true
    )
    private Set<Application> applications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public SocialMedia title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public SocialMedia url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public SocialMedia icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColour() {
        return this.colour;
    }

    public SocialMedia colour(String colour) {
        this.setColour(colour);
        return this;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeSocialMedia(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addSocialMedia(this));
        }
        this.applications = applications;
    }

    public SocialMedia applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public SocialMedia addApplication(Application application) {
        this.applications.add(application);
        application.getSocialMedias().add(this);
        return this;
    }

    public SocialMedia removeApplication(Application application) {
        this.applications.remove(application);
        application.getSocialMedias().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((SocialMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialMedia{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", url='" + getUrl() + "'" +
            ", icon='" + getIcon() + "'" +
            ", colour='" + getColour() + "'" +
            "}";
    }
}
