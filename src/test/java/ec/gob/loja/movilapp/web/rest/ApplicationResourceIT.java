package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.ApplicationAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.repository.ApplicationRepository;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.service.ApplicationService;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import ec.gob.loja.movilapp.service.mapper.ApplicationMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ApplicationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL_ANDROID = "AAAAAAAAAA";
    private static final String UPDATED_URL_ANDROID = "BBBBBBBBBB";

    private static final String DEFAULT_URL_IOS = "AAAAAAAAAA";
    private static final String UPDATED_URL_IOS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_VERSION = 1D;
    private static final Double UPDATED_VERSION = 2D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationRepository applicationRepositoryMock;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Mock
    private ApplicationService applicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Application application;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createEntity(EntityManager em) {
        Application application = new Application()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .urlAndroid(DEFAULT_URL_ANDROID)
            .urlIos(DEFAULT_URL_IOS)
            .description(DEFAULT_DESCRIPTION)
            .version(DEFAULT_VERSION)
            .isActive(DEFAULT_IS_ACTIVE);
        return application;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Application createUpdatedEntity(EntityManager em) {
        Application application = new Application()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .urlAndroid(UPDATED_URL_ANDROID)
            .urlIos(UPDATED_URL_IOS)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE);
        return application;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_application__banner").block();
            em.deleteAll("rel_application__colour_palette").block();
            em.deleteAll("rel_application__social_media").block();
            em.deleteAll(Application.class).block();
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
        application = createEntity(em);
    }

    @Test
    void createApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);
        var returnedApplicationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ApplicationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Application in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApplication = applicationMapper.toEntity(returnedApplicationDTO);
        assertApplicationUpdatableFieldsEquals(returnedApplication, getPersistedApplication(returnedApplication));
    }

    @Test
    void createApplicationWithExistingId() throws Exception {
        // Create the Application with an existing ID
        application.setId(1L);
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setCode(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        application.setName(null);

        // Create the Application, which fails.
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllApplications() {
        // Initialize the database
        applicationRepository.save(application).block();

        // Get all the applicationList
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
            .value(hasItem(application.getId().intValue()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].urlAndroid")
            .value(hasItem(DEFAULT_URL_ANDROID))
            .jsonPath("$.[*].urlIos")
            .value(hasItem(DEFAULT_URL_IOS))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].version")
            .value(hasItem(DEFAULT_VERSION.doubleValue()))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationsWithEagerRelationshipsIsEnabled() {
        when(applicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(applicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllApplicationsWithEagerRelationshipsIsNotEnabled() {
        when(applicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(applicationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getApplication() {
        // Initialize the database
        applicationRepository.save(application).block();

        // Get the application
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, application.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(application.getId().intValue()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.urlAndroid")
            .value(is(DEFAULT_URL_ANDROID))
            .jsonPath("$.urlIos")
            .value(is(DEFAULT_URL_IOS))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.version")
            .value(is(DEFAULT_VERSION.doubleValue()))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingApplication() {
        // Get the application
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingApplication() throws Exception {
        // Initialize the database
        applicationRepository.save(application).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application
        Application updatedApplication = applicationRepository.findById(application.getId()).block();
        updatedApplication
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .urlAndroid(UPDATED_URL_ANDROID)
            .urlIos(UPDATED_URL_IOS)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE);
        ApplicationDTO applicationDTO = applicationMapper.toDto(updatedApplication);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, applicationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApplicationToMatchAllProperties(updatedApplication);
    }

    @Test
    void putNonExistingApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, applicationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.save(application).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication.code(UPDATED_CODE).urlIos(UPDATED_URL_IOS).isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedApplication))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Application in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApplication, application),
            getPersistedApplication(application)
        );
    }

    @Test
    void fullUpdateApplicationWithPatch() throws Exception {
        // Initialize the database
        applicationRepository.save(application).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the application using partial update
        Application partialUpdatedApplication = new Application();
        partialUpdatedApplication.setId(application.getId());

        partialUpdatedApplication
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .urlAndroid(UPDATED_URL_ANDROID)
            .urlIos(UPDATED_URL_IOS)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedApplication.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedApplication))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Application in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApplicationUpdatableFieldsEquals(partialUpdatedApplication, getPersistedApplication(partialUpdatedApplication));
    }

    @Test
    void patchNonExistingApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, applicationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        application.setId(longCount.incrementAndGet());

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.toDto(application);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(applicationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Application in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApplication() {
        // Initialize the database
        applicationRepository.save(application).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the application
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, application.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return applicationRepository.count().block();
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

    protected Application getPersistedApplication(Application application) {
        return applicationRepository.findById(application.getId()).block();
    }

    protected void assertPersistedApplicationToMatchAllProperties(Application expectedApplication) {
        // Test fails because reactive api returns an empty object instead of null
        // assertApplicationAllPropertiesEquals(expectedApplication, getPersistedApplication(expectedApplication));
        assertApplicationUpdatableFieldsEquals(expectedApplication, getPersistedApplication(expectedApplication));
    }

    protected void assertPersistedApplicationToMatchUpdatableProperties(Application expectedApplication) {
        // Test fails because reactive api returns an empty object instead of null
        // assertApplicationAllUpdatablePropertiesEquals(expectedApplication, getPersistedApplication(expectedApplication));
        assertApplicationUpdatableFieldsEquals(expectedApplication, getPersistedApplication(expectedApplication));
    }
}
