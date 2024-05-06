package ec.gob.loja.movilapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.AppBanner} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppBannerDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @Lob
    private byte[] bannerImage;

    private String bannerImageContentType;

    @NotNull(message = "must not be null")
    private Boolean isActive;

    @NotNull(message = "must not be null")
    private LocalDate initDate;

    @NotNull(message = "must not be null")
    private LocalDate endDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime initTime;

    private ZonedDateTime endTime;

    private String description;

    private String url;

    private ZonedDateTime createdAt;

    private ZonedDateTime modificationDate;

    private Integer priority;

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

    public byte[] getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(byte[] bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerImageContentType() {
        return bannerImageContentType;
    }

    public void setBannerImageContentType(String bannerImageContentType) {
        this.bannerImageContentType = bannerImageContentType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getInitTime() {
        return initTime;
    }

    public void setInitTime(ZonedDateTime initTime) {
        this.initTime = initTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
        if (!(o instanceof AppBannerDTO)) {
            return false;
        }

        AppBannerDTO appBannerDTO = (AppBannerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appBannerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppBannerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", bannerImage='" + getBannerImage() + "'" +
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
            ", applications=" + getApplications() +
            "}";
    }
}
