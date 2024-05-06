package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AppColourPalette.
 */
@Table("app_colour_palette")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppColourPalette implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("description")
    private String description;

    @Column("primary_colour")
    private String primaryColour;

    @Column("secondary_colour")
    private String secondaryColour;

    @Column("tertiary_colour")
    private String tertiaryColour;

    @Column("neutral_colour")
    private String neutralColour;

    @Column("ligth_background_colour")
    private String ligthBackgroundColour;

    @Column("dark_background_colour")
    private String darkBackgroundColour;

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

    public AppColourPalette id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public AppColourPalette description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryColour() {
        return this.primaryColour;
    }

    public AppColourPalette primaryColour(String primaryColour) {
        this.setPrimaryColour(primaryColour);
        return this;
    }

    public void setPrimaryColour(String primaryColour) {
        this.primaryColour = primaryColour;
    }

    public String getSecondaryColour() {
        return this.secondaryColour;
    }

    public AppColourPalette secondaryColour(String secondaryColour) {
        this.setSecondaryColour(secondaryColour);
        return this;
    }

    public void setSecondaryColour(String secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public String getTertiaryColour() {
        return this.tertiaryColour;
    }

    public AppColourPalette tertiaryColour(String tertiaryColour) {
        this.setTertiaryColour(tertiaryColour);
        return this;
    }

    public void setTertiaryColour(String tertiaryColour) {
        this.tertiaryColour = tertiaryColour;
    }

    public String getNeutralColour() {
        return this.neutralColour;
    }

    public AppColourPalette neutralColour(String neutralColour) {
        this.setNeutralColour(neutralColour);
        return this;
    }

    public void setNeutralColour(String neutralColour) {
        this.neutralColour = neutralColour;
    }

    public String getLigthBackgroundColour() {
        return this.ligthBackgroundColour;
    }

    public AppColourPalette ligthBackgroundColour(String ligthBackgroundColour) {
        this.setLigthBackgroundColour(ligthBackgroundColour);
        return this;
    }

    public void setLigthBackgroundColour(String ligthBackgroundColour) {
        this.ligthBackgroundColour = ligthBackgroundColour;
    }

    public String getDarkBackgroundColour() {
        return this.darkBackgroundColour;
    }

    public AppColourPalette darkBackgroundColour(String darkBackgroundColour) {
        this.setDarkBackgroundColour(darkBackgroundColour);
        return this;
    }

    public void setDarkBackgroundColour(String darkBackgroundColour) {
        this.darkBackgroundColour = darkBackgroundColour;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeColourPalette(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addColourPalette(this));
        }
        this.applications = applications;
    }

    public AppColourPalette applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public AppColourPalette addApplication(Application application) {
        this.applications.add(application);
        application.getColourPalettes().add(this);
        return this;
    }

    public AppColourPalette removeApplication(Application application) {
        this.applications.remove(application);
        application.getColourPalettes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppColourPalette)) {
            return false;
        }
        return getId() != null && getId().equals(((AppColourPalette) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppColourPalette{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", primaryColour='" + getPrimaryColour() + "'" +
            ", secondaryColour='" + getSecondaryColour() + "'" +
            ", tertiaryColour='" + getTertiaryColour() + "'" +
            ", neutralColour='" + getNeutralColour() + "'" +
            ", ligthBackgroundColour='" + getLigthBackgroundColour() + "'" +
            ", darkBackgroundColour='" + getDarkBackgroundColour() + "'" +
            "}";
    }
}
