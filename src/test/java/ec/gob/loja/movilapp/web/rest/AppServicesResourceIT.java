package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.AppServicesAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.AppServices;
import ec.gob.loja.movilapp.repository.AppServicesRepository;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.service.dto.AppServicesDTO;
import ec.gob.loja.movilapp.service.mapper.AppServicesMapper;
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
 * Integration tests for the {@link AppServicesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppServicesResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_BACKGROUND_CARD = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_CARD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String ENTITY_API_URL = "/api/app-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppServicesRepository appServicesRepository;

    @Autowired
    private AppServicesMapper appServicesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppServices appServices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppServices createEntity(EntityManager em) {
        AppServices appServices = new AppServices()
            .title(DEFAULT_TITLE)
            .url(DEFAULT_URL)
            .icon(DEFAULT_ICON)
            .backgroundCard(DEFAULT_BACKGROUND_CARD)
            .isActive(DEFAULT_IS_ACTIVE)
            .priority(DEFAULT_PRIORITY);
        return appServices;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppServices createUpdatedEntity(EntityManager em) {
        AppServices appServices = new AppServices()
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .backgroundCard(UPDATED_BACKGROUND_CARD)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY);
        return appServices;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppServices.class).block();
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
        appServices = createEntity(em);
    }

    @Test
    void createAppServices() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);
        var returnedAppServicesDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AppServicesDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AppServices in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppServices = appServicesMapper.toEntity(returnedAppServicesDTO);
        assertAppServicesUpdatableFieldsEquals(returnedAppServices, getPersistedAppServices(returnedAppServices));
    }

    @Test
    void createAppServicesWithExistingId() throws Exception {
        // Create the AppServices with an existing ID
        appServices.setId(1L);
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appServices.setTitle(null);

        // Create the AppServices, which fails.
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appServices.setUrl(null);

        // Create the AppServices, which fails.
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIconIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appServices.setIcon(null);

        // Create the AppServices, which fails.
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppServices() {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        // Get all the appServicesList
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
            .value(hasItem(appServices.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].backgroundCard")
            .value(hasItem(DEFAULT_BACKGROUND_CARD))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].priority")
            .value(hasItem(DEFAULT_PRIORITY));
    }

    @Test
    void getAppServices() {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        // Get the appServices
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appServices.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appServices.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.backgroundCard")
            .value(is(DEFAULT_BACKGROUND_CARD))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.priority")
            .value(is(DEFAULT_PRIORITY));
    }

    @Test
    void getNonExistingAppServices() {
        // Get the appServices
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppServices() throws Exception {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appServices
        AppServices updatedAppServices = appServicesRepository.findById(appServices.getId()).block();
        updatedAppServices
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .backgroundCard(UPDATED_BACKGROUND_CARD)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY);
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(updatedAppServices);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appServicesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppServicesToMatchAllProperties(updatedAppServices);
    }

    @Test
    void putNonExistingAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appServicesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppServicesWithPatch() throws Exception {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appServices using partial update
        AppServices partialUpdatedAppServices = new AppServices();
        partialUpdatedAppServices.setId(appServices.getId());

        partialUpdatedAppServices.isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppServices.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppServices))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppServices in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppServicesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppServices, appServices),
            getPersistedAppServices(appServices)
        );
    }

    @Test
    void fullUpdateAppServicesWithPatch() throws Exception {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appServices using partial update
        AppServices partialUpdatedAppServices = new AppServices();
        partialUpdatedAppServices.setId(appServices.getId());

        partialUpdatedAppServices
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .backgroundCard(UPDATED_BACKGROUND_CARD)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppServices.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppServices))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppServices in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppServicesUpdatableFieldsEquals(partialUpdatedAppServices, getPersistedAppServices(partialUpdatedAppServices));
    }

    @Test
    void patchNonExistingAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appServicesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appServices.setId(longCount.incrementAndGet());

        // Create the AppServices
        AppServicesDTO appServicesDTO = appServicesMapper.toDto(appServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appServicesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppServices() {
        // Initialize the database
        appServicesRepository.save(appServices).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appServices
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appServices.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appServicesRepository.count().block();
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

    protected AppServices getPersistedAppServices(AppServices appServices) {
        return appServicesRepository.findById(appServices.getId()).block();
    }

    protected void assertPersistedAppServicesToMatchAllProperties(AppServices expectedAppServices) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppServicesAllPropertiesEquals(expectedAppServices, getPersistedAppServices(expectedAppServices));
        assertAppServicesUpdatableFieldsEquals(expectedAppServices, getPersistedAppServices(expectedAppServices));
    }

    protected void assertPersistedAppServicesToMatchUpdatableProperties(AppServices expectedAppServices) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppServicesAllUpdatablePropertiesEquals(expectedAppServices, getPersistedAppServices(expectedAppServices));
        assertAppServicesUpdatableFieldsEquals(expectedAppServices, getPersistedAppServices(expectedAppServices));
    }
}
