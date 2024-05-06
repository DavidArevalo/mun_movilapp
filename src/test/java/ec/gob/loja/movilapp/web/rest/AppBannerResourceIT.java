package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.AppBannerAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static ec.gob.loja.movilapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.AppBanner;
import ec.gob.loja.movilapp.repository.AppBannerRepository;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
import ec.gob.loja.movilapp.service.mapper.AppBannerMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AppBannerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppBannerResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BANNER_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BANNER_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BANNER_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BANNER_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_INIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INIT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_INIT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INIT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/app-banners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppBannerRepository appBannerRepository;

    @Autowired
    private AppBannerMapper appBannerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppBanner appBanner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppBanner createEntity(EntityManager em) {
        AppBanner appBanner = new AppBanner()
            .title(DEFAULT_TITLE)
            .bannerImage(DEFAULT_BANNER_IMAGE)
            .bannerImageContentType(DEFAULT_BANNER_IMAGE_CONTENT_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .initDate(DEFAULT_INIT_DATE)
            .endDate(DEFAULT_END_DATE)
            .initTime(DEFAULT_INIT_TIME)
            .endTime(DEFAULT_END_TIME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .createdAt(DEFAULT_CREATED_AT)
            .modificationDate(DEFAULT_MODIFICATION_DATE)
            .priority(DEFAULT_PRIORITY);
        return appBanner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppBanner createUpdatedEntity(EntityManager em) {
        AppBanner appBanner = new AppBanner()
            .title(UPDATED_TITLE)
            .bannerImage(UPDATED_BANNER_IMAGE)
            .bannerImageContentType(UPDATED_BANNER_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .initTime(UPDATED_INIT_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .priority(UPDATED_PRIORITY);
        return appBanner;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppBanner.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        appBanner = createEntity(em);
    }

    @Test
    void createAppBanner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);
        var returnedAppBannerDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AppBannerDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AppBanner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppBanner = appBannerMapper.toEntity(returnedAppBannerDTO);
        assertAppBannerUpdatableFieldsEquals(returnedAppBanner, getPersistedAppBanner(returnedAppBanner));
    }

    @Test
    void createAppBannerWithExistingId() throws Exception {
        // Create the AppBanner with an existing ID
        appBanner.setId(1L);
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appBanner.setTitle(null);

        // Create the AppBanner, which fails.
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appBanner.setIsActive(null);

        // Create the AppBanner, which fails.
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInitDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appBanner.setInitDate(null);

        // Create the AppBanner, which fails.
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appBanner.setEndDate(null);

        // Create the AppBanner, which fails.
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInitTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appBanner.setInitTime(null);

        // Create the AppBanner, which fails.
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppBanners() {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        // Get all the appBannerList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(appBanner.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].bannerImageContentType")
            .value(hasItem(DEFAULT_BANNER_IMAGE_CONTENT_TYPE))
            .jsonPath("$.[*].bannerImage")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BANNER_IMAGE)))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].initDate")
            .value(hasItem(DEFAULT_INIT_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].initTime")
            .value(hasItem(sameInstant(DEFAULT_INIT_TIME)))
            .jsonPath("$.[*].endTime")
            .value(hasItem(sameInstant(DEFAULT_END_TIME)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.[*].modificationDate")
            .value(hasItem(sameInstant(DEFAULT_MODIFICATION_DATE)))
            .jsonPath("$.[*].priority")
            .value(hasItem(DEFAULT_PRIORITY));
    }

    @Test
    void getAppBanner() {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        // Get the appBanner
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appBanner.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appBanner.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.bannerImageContentType")
            .value(is(DEFAULT_BANNER_IMAGE_CONTENT_TYPE))
            .jsonPath("$.bannerImage")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_BANNER_IMAGE)))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.initDate")
            .value(is(DEFAULT_INIT_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.initTime")
            .value(is(sameInstant(DEFAULT_INIT_TIME)))
            .jsonPath("$.endTime")
            .value(is(sameInstant(DEFAULT_END_TIME)))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.createdAt")
            .value(is(sameInstant(DEFAULT_CREATED_AT)))
            .jsonPath("$.modificationDate")
            .value(is(sameInstant(DEFAULT_MODIFICATION_DATE)))
            .jsonPath("$.priority")
            .value(is(DEFAULT_PRIORITY));
    }

    @Test
    void getNonExistingAppBanner() {
        // Get the appBanner
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppBanner() throws Exception {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appBanner
        AppBanner updatedAppBanner = appBannerRepository.findById(appBanner.getId()).block();
        updatedAppBanner
            .title(UPDATED_TITLE)
            .bannerImage(UPDATED_BANNER_IMAGE)
            .bannerImageContentType(UPDATED_BANNER_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .initTime(UPDATED_INIT_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .priority(UPDATED_PRIORITY);
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(updatedAppBanner);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appBannerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppBannerToMatchAllProperties(updatedAppBanner);
    }

    @Test
    void putNonExistingAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appBannerDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppBannerWithPatch() throws Exception {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appBanner using partial update
        AppBanner partialUpdatedAppBanner = new AppBanner();
        partialUpdatedAppBanner.setId(appBanner.getId());

        partialUpdatedAppBanner
            .title(UPDATED_TITLE)
            .isActive(UPDATED_IS_ACTIVE)
            .endDate(UPDATED_END_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .priority(UPDATED_PRIORITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppBanner.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppBanner))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppBanner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppBannerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppBanner, appBanner),
            getPersistedAppBanner(appBanner)
        );
    }

    @Test
    void fullUpdateAppBannerWithPatch() throws Exception {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appBanner using partial update
        AppBanner partialUpdatedAppBanner = new AppBanner();
        partialUpdatedAppBanner.setId(appBanner.getId());

        partialUpdatedAppBanner
            .title(UPDATED_TITLE)
            .bannerImage(UPDATED_BANNER_IMAGE)
            .bannerImageContentType(UPDATED_BANNER_IMAGE_CONTENT_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .initDate(UPDATED_INIT_DATE)
            .endDate(UPDATED_END_DATE)
            .initTime(UPDATED_INIT_TIME)
            .endTime(UPDATED_END_TIME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .priority(UPDATED_PRIORITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppBanner.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppBanner))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppBanner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppBannerUpdatableFieldsEquals(partialUpdatedAppBanner, getPersistedAppBanner(partialUpdatedAppBanner));
    }

    @Test
    void patchNonExistingAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appBannerDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppBanner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appBanner.setId(longCount.incrementAndGet());

        // Create the AppBanner
        AppBannerDTO appBannerDTO = appBannerMapper.toDto(appBanner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appBannerDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppBanner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppBanner() {
        // Initialize the database
        appBannerRepository.save(appBanner).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appBanner
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appBanner.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appBannerRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AppBanner getPersistedAppBanner(AppBanner appBanner) {
        return appBannerRepository.findById(appBanner.getId()).block();
    }

    protected void assertPersistedAppBannerToMatchAllProperties(AppBanner expectedAppBanner) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppBannerAllPropertiesEquals(expectedAppBanner, getPersistedAppBanner(expectedAppBanner));
        assertAppBannerUpdatableFieldsEquals(expectedAppBanner, getPersistedAppBanner(expectedAppBanner));
    }

    protected void assertPersistedAppBannerToMatchUpdatableProperties(AppBanner expectedAppBanner) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppBannerAllUpdatablePropertiesEquals(expectedAppBanner, getPersistedAppBanner(expectedAppBanner));
        assertAppBannerUpdatableFieldsEquals(expectedAppBanner, getPersistedAppBanner(expectedAppBanner));
    }
}
