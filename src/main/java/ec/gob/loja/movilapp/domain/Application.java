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
 * START MOVILAPP
 */
@Table("application")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("url_android")
    private String urlAndroid;

    @Column("url_ios")
    private String urlIos;

    @Column("description")
    private String description;

    @Column("version")
    private Double version;

    @Column("is_active")
    private Boolean isActive;

    @Transient
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<AppServices> services = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<AppMenu> menus = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "application" }, allowSetters = true)
    private Set<FrequentlyQuestion> frequentlyQuestions = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private Set<AppBanner> banners = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private Set<AppColourPalette> colourPalettes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "applications" }, allowSetters = true)
    private Set<SocialMedia> socialMedias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Application id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Application code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Application name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAndroid() {
        return this.urlAndroid;
    }

    public Application urlAndroid(String urlAndroid) {
        this.setUrlAndroid(urlAndroid);
        return this;
    }

    public void setUrlAndroid(String urlAndroid) {
        this.urlAndroid = urlAndroid;
    }

    public String getUrlIos() {
        return this.urlIos;
    }

    public Application urlIos(String urlIos) {
        this.setUrlIos(urlIos);
        return this;
    }

    public void setUrlIos(String urlIos) {
        this.urlIos = urlIos;
    }

    public String getDescription() {
        return this.description;
    }

    public Application description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getVersion() {
        return this.version;
    }

    public Application version(Double version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Application isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AppServices> getServices() {
        return this.services;
    }

    public void setServices(Set<AppServices> appServices) {
        if (this.services != null) {
            this.services.forEach(i -> i.setApplication(null));
        }
        if (appServices != null) {
            appServices.forEach(i -> i.setApplication(this));
        }
        this.services = appServices;
    }

    public Application services(Set<AppServices> appServices) {
        this.setServices(appServices);
        return this;
    }

    public Application addService(AppServices appServices) {
        this.services.add(appServices);
        appServices.setApplication(this);
        return this;
    }

    public Application removeService(AppServices appServices) {
        this.services.remove(appServices);
        appServices.setApplication(null);
        return this;
    }

    public Set<AppMenu> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<AppMenu> appMenus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.setApplication(null));
        }
        if (appMenus != null) {
            appMenus.forEach(i -> i.setApplication(this));
        }
        this.menus = appMenus;
    }

    public Application menus(Set<AppMenu> appMenus) {
        this.setMenus(appMenus);
        return this;
    }

    public Application addMenu(AppMenu appMenu) {
        this.menus.add(appMenu);
        appMenu.setApplication(this);
        return this;
    }

    public Application removeMenu(AppMenu appMenu) {
        this.menus.remove(appMenu);
        appMenu.setApplication(null);
        return this;
    }

    public Set<FrequentlyQuestion> getFrequentlyQuestions() {
        return this.frequentlyQuestions;
    }

    public void setFrequentlyQuestions(Set<FrequentlyQuestion> frequentlyQuestions) {
        if (this.frequentlyQuestions != null) {
            this.frequentlyQuestions.forEach(i -> i.setApplication(null));
        }
        if (frequentlyQuestions != null) {
            frequentlyQuestions.forEach(i -> i.setApplication(this));
        }
        this.frequentlyQuestions = frequentlyQuestions;
    }

    public Application frequentlyQuestions(Set<FrequentlyQuestion> frequentlyQuestions) {
        this.setFrequentlyQuestions(frequentlyQuestions);
        return this;
    }

    public Application addFrequentlyQuestion(FrequentlyQuestion frequentlyQuestion) {
        this.frequentlyQuestions.add(frequentlyQuestion);
        frequentlyQuestion.setApplication(this);
        return this;
    }

    public Application removeFrequentlyQuestion(FrequentlyQuestion frequentlyQuestion) {
        this.frequentlyQuestions.remove(frequentlyQuestion);
        frequentlyQuestion.setApplication(null);
        return this;
    }

    public Set<AppBanner> getBanners() {
        return this.banners;
    }

    public void setBanners(Set<AppBanner> appBanners) {
        this.banners = appBanners;
    }

    public Application banners(Set<AppBanner> appBanners) {
        this.setBanners(appBanners);
        return this;
    }

    public Application addBanner(AppBanner appBanner) {
        this.banners.add(appBanner);
        return this;
    }

    public Application removeBanner(AppBanner appBanner) {
        this.banners.remove(appBanner);
        return this;
    }

    public Set<AppColourPalette> getColourPalettes() {
        return this.colourPalettes;
    }

    public void setColourPalettes(Set<AppColourPalette> appColourPalettes) {
        this.colourPalettes = appColourPalettes;
    }

    public Application colourPalettes(Set<AppColourPalette> appColourPalettes) {
        this.setColourPalettes(appColourPalettes);
        return this;
    }

    public Application addColourPalette(AppColourPalette appColourPalette) {
        this.colourPalettes.add(appColourPalette);
        return this;
    }

    public Application removeColourPalette(AppColourPalette appColourPalette) {
        this.colourPalettes.remove(appColourPalette);
        return this;
    }

    public Set<SocialMedia> getSocialMedias() {
        return this.socialMedias;
    }

    public void setSocialMedias(Set<SocialMedia> socialMedias) {
        this.socialMedias = socialMedias;
    }

    public Application socialMedias(Set<SocialMedia> socialMedias) {
        this.setSocialMedias(socialMedias);
        return this;
    }

    public Application addSocialMedia(SocialMedia socialMedia) {
        this.socialMedias.add(socialMedia);
        return this;
    }

    public Application removeSocialMedia(SocialMedia socialMedia) {
        this.socialMedias.remove(socialMedia);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return getId() != null && getId().equals(((Application) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", urlAndroid='" + getUrlAndroid() + "'" +
            ", urlIos='" + getUrlIos() + "'" +
            ", description='" + getDescription() + "'" +
            ", version=" + getVersion() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
