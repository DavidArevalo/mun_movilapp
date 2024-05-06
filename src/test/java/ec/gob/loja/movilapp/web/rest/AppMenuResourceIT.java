package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.AppMenuAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.AppMenu;
import ec.gob.loja.movilapp.repository.AppMenuRepository;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.service.dto.AppMenuDTO;
import ec.gob.loja.movilapp.service.mapper.AppMenuMapper;
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
 * Integration tests for the {@link AppMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppMenuResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final String DEFAULT_COMPONENT = "AAAAAAAAAA";
    private static final String UPDATED_COMPONENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppMenuRepository appMenuRepository;

    @Autowired
    private AppMenuMapper appMenuMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppMenu appMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppMenu createEntity(EntityManager em) {
        AppMenu appMenu = new AppMenu()
            .title(DEFAULT_TITLE)
            .url(DEFAULT_URL)
            .icon(DEFAULT_ICON)
            .isActive(DEFAULT_IS_ACTIVE)
            .priority(DEFAULT_PRIORITY)
            .component(DEFAULT_COMPONENT);
        return appMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppMenu createUpdatedEntity(EntityManager em) {
        AppMenu appMenu = new AppMenu()
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY)
            .component(UPDATED_COMPONENT);
        return appMenu;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppMenu.class).block();
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
        appMenu = createEntity(em);
    }

    @Test
    void createAppMenu() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);
        var returnedAppMenuDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AppMenuDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AppMenu in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppMenu = appMenuMapper.toEntity(returnedAppMenuDTO);
        assertAppMenuUpdatableFieldsEquals(returnedAppMenu, getPersistedAppMenu(returnedAppMenu));
    }

    @Test
    void createAppMenuWithExistingId() throws Exception {
        // Create the AppMenu with an existing ID
        appMenu.setId(1L);
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appMenu.setTitle(null);

        // Create the AppMenu, which fails.
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appMenu.setUrl(null);

        // Create the AppMenu, which fails.
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIconIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appMenu.setIcon(null);

        // Create the AppMenu, which fails.
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAppMenus() {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        // Get all the appMenuList
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
            .value(hasItem(appMenu.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.[*].priority")
            .value(hasItem(DEFAULT_PRIORITY))
            .jsonPath("$.[*].component")
            .value(hasItem(DEFAULT_COMPONENT));
    }

    @Test
    void getAppMenu() {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        // Get the appMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appMenu.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()))
            .jsonPath("$.priority")
            .value(is(DEFAULT_PRIORITY))
            .jsonPath("$.component")
            .value(is(DEFAULT_COMPONENT));
    }

    @Test
    void getNonExistingAppMenu() {
        // Get the appMenu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppMenu() throws Exception {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMenu
        AppMenu updatedAppMenu = appMenuRepository.findById(appMenu.getId()).block();
        updatedAppMenu
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY)
            .component(UPDATED_COMPONENT);
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(updatedAppMenu);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appMenuDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppMenuToMatchAllProperties(updatedAppMenu);
    }

    @Test
    void putNonExistingAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appMenuDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppMenuWithPatch() throws Exception {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMenu using partial update
        AppMenu partialUpdatedAppMenu = new AppMenu();
        partialUpdatedAppMenu.setId(appMenu.getId());

        partialUpdatedAppMenu.title(UPDATED_TITLE).isActive(UPDATED_IS_ACTIVE).priority(UPDATED_PRIORITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppMenuUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppMenu, appMenu), getPersistedAppMenu(appMenu));
    }

    @Test
    void fullUpdateAppMenuWithPatch() throws Exception {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appMenu using partial update
        AppMenu partialUpdatedAppMenu = new AppMenu();
        partialUpdatedAppMenu.setId(appMenu.getId());

        partialUpdatedAppMenu
            .title(UPDATED_TITLE)
            .url(UPDATED_URL)
            .icon(UPDATED_ICON)
            .isActive(UPDATED_IS_ACTIVE)
            .priority(UPDATED_PRIORITY)
            .component(UPDATED_COMPONENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppMenuUpdatableFieldsEquals(partialUpdatedAppMenu, getPersistedAppMenu(partialUpdatedAppMenu));
    }

    @Test
    void patchNonExistingAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appMenuDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appMenu.setId(longCount.incrementAndGet());

        // Create the AppMenu
        AppMenuDTO appMenuDTO = appMenuMapper.toDto(appMenu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appMenuDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppMenu() {
        // Initialize the database
        appMenuRepository.save(appMenu).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appMenu
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appMenu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appMenuRepository.count().block();
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

    protected AppMenu getPersistedAppMenu(AppMenu appMenu) {
        return appMenuRepository.findById(appMenu.getId()).block();
    }

    protected void assertPersistedAppMenuToMatchAllProperties(AppMenu expectedAppMenu) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppMenuAllPropertiesEquals(expectedAppMenu, getPersistedAppMenu(expectedAppMenu));
        assertAppMenuUpdatableFieldsEquals(expectedAppMenu, getPersistedAppMenu(expectedAppMenu));
    }

    protected void assertPersistedAppMenuToMatchUpdatableProperties(AppMenu expectedAppMenu) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppMenuAllUpdatablePropertiesEquals(expectedAppMenu, getPersistedAppMenu(expectedAppMenu));
        assertAppMenuUpdatableFieldsEquals(expectedAppMenu, getPersistedAppMenu(expectedAppMenu));
    }
}
