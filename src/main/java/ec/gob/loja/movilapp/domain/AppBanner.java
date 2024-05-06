package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AppBanner.
 */
@Table("app_banner")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("title")
    private String title;

    @Column("banner_image")
    private byte[] bannerImage;

    @NotNull
    @Column("banner_image_content_type")
    private String bannerImageContentType;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @NotNull(message = "must not be null")
    @Column("init_date")
    private LocalDate initDate;

    @NotNull(message = "must not be null")
    @Column("end_date")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    @Column("init_time")
    private ZonedDateTime initTime;

    @Column("end_time")
    private ZonedDateTime endTime;

    @Column("description")
    private String description;

    @Column("url")
    private String url;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("modification_date")
    private ZonedDateTime modificationDate;

    @Column("priority")
    private Integer priority;

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

    public AppBanner id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AppBanner title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getBannerImage() {
        return this.bannerImage;
    }

    public AppBanner bannerImage(byte[] bannerImage) {
        this.setBannerImage(bannerImage);
        return this;
    }

    public void setBannerImage(byte[] bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerImageContentType() {
        return this.bannerImageContentType;
    }

    public AppBanner bannerImageContentType(String bannerImageContentType) {
        this.bannerImageContentType = bannerImageContentType;
        return this;
    }

    public void setBannerImageContentType(String bannerImageContentType) {
        this.bannerImageContentType = bannerImageContentType;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppBanner isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getInitDate() {
        return this.initDate;
    }

    public AppBanner initDate(LocalDate initDate) {
        this.setInitDate(initDate);
        return this;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public AppBanner endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getInitTime() {
        return this.initTime;
    }

    public AppBanner initTime(ZonedDateTime initTime) {
        this.setInitTime(initTime);
        return this;
    }

    public void setInitTime(ZonedDateTime initTime) {
        this.initTime = initTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public AppBanner endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return this.description;
    }

    public AppBanner description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public AppBanner url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public AppBanner createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModificationDate() {
        return this.modificationDate;
    }

    public AppBanner modificationDate(ZonedDateTime modificationDate) {
        this.setModificationDate(modificationDate);
        return this;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public AppBanner priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Set<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(Set<Application> applications) {
        if (this.applications != null) {
            this.applications.forEach(i -> i.removeBanner(this));
        }
        if (applications != null) {
            applications.forEach(i -> i.addBanner(this));
        }
        this.applications = applications;
    }

    public AppBanner applications(Set<Application> applications) {
        this.setApplications(applications);
        return this;
    }

    public AppBanner addApplication(Application application) {
        this.applications.add(application);
        application.getBanners().add(this);
        return this;
    }

    public AppBanner removeApplication(Application application) {
        this.applications.remove(application);
        application.getBanners().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppBanner)) {
            return false;
        }
        return getId() != null && getId().equals(((AppBanner) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppBanner{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", bannerImage='" + getBannerImage() + "'" +
            ", bannerImageContentType='" + getBannerImageContentType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", initDate='" + getInitDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", initTime='" + getInitTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
