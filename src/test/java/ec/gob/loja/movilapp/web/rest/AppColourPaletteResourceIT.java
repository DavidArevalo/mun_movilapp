package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.AppColourPaletteAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.AppColourPalette;
import ec.gob.loja.movilapp.repository.AppColourPaletteRepository;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import ec.gob.loja.movilapp.service.mapper.AppColourPaletteMapper;
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
 * Integration tests for the {@link AppColourPaletteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AppColourPaletteResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_TERTIARY_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_TERTIARY_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_NEUTRAL_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_NEUTRAL_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_LIGTH_BACKGROUND_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_LIGTH_BACKGROUND_COLOUR = "BBBBBBBBBB";

    private static final String DEFAULT_DARK_BACKGROUND_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_DARK_BACKGROUND_COLOUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-colour-palettes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppColourPaletteRepository appColourPaletteRepository;

    @Autowired
    private AppColourPaletteMapper appColourPaletteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AppColourPalette appColourPalette;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppColourPalette createEntity(EntityManager em) {
        AppColourPalette appColourPalette = new AppColourPalette()
            .description(DEFAULT_DESCRIPTION)
            .primaryColour(DEFAULT_PRIMARY_COLOUR)
            .secondaryColour(DEFAULT_SECONDARY_COLOUR)
            .tertiaryColour(DEFAULT_TERTIARY_COLOUR)
            .neutralColour(DEFAULT_NEUTRAL_COLOUR)
            .ligthBackgroundColour(DEFAULT_LIGTH_BACKGROUND_COLOUR)
            .darkBackgroundColour(DEFAULT_DARK_BACKGROUND_COLOUR);
        return appColourPalette;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppColourPalette createUpdatedEntity(EntityManager em) {
        AppColourPalette appColourPalette = new AppColourPalette()
            .description(UPDATED_DESCRIPTION)
            .primaryColour(UPDATED_PRIMARY_COLOUR)
            .secondaryColour(UPDATED_SECONDARY_COLOUR)
            .tertiaryColour(UPDATED_TERTIARY_COLOUR)
            .neutralColour(UPDATED_NEUTRAL_COLOUR)
            .ligthBackgroundColour(UPDATED_LIGTH_BACKGROUND_COLOUR)
            .darkBackgroundColour(UPDATED_DARK_BACKGROUND_COLOUR);
        return appColourPalette;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AppColourPalette.class).block();
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
        appColourPalette = createEntity(em);
    }

    @Test
    void createAppColourPalette() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);
        var returnedAppColourPaletteDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AppColourPaletteDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the AppColourPalette in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppColourPalette = appColourPaletteMapper.toEntity(returnedAppColourPaletteDTO);
        assertAppColourPaletteUpdatableFieldsEquals(returnedAppColourPalette, getPersistedAppColourPalette(returnedAppColourPalette));
    }

    @Test
    void createAppColourPaletteWithExistingId() throws Exception {
        // Create the AppColourPalette with an existing ID
        appColourPalette.setId(1L);
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAppColourPalettes() {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        // Get all the appColourPaletteList
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
            .value(hasItem(appColourPalette.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].primaryColour")
            .value(hasItem(DEFAULT_PRIMARY_COLOUR))
            .jsonPath("$.[*].secondaryColour")
            .value(hasItem(DEFAULT_SECONDARY_COLOUR))
            .jsonPath("$.[*].tertiaryColour")
            .value(hasItem(DEFAULT_TERTIARY_COLOUR))
            .jsonPath("$.[*].neutralColour")
            .value(hasItem(DEFAULT_NEUTRAL_COLOUR))
            .jsonPath("$.[*].ligthBackgroundColour")
            .value(hasItem(DEFAULT_LIGTH_BACKGROUND_COLOUR))
            .jsonPath("$.[*].darkBackgroundColour")
            .value(hasItem(DEFAULT_DARK_BACKGROUND_COLOUR));
    }

    @Test
    void getAppColourPalette() {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        // Get the appColourPalette
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, appColourPalette.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(appColourPalette.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.primaryColour")
            .value(is(DEFAULT_PRIMARY_COLOUR))
            .jsonPath("$.secondaryColour")
            .value(is(DEFAULT_SECONDARY_COLOUR))
            .jsonPath("$.tertiaryColour")
            .value(is(DEFAULT_TERTIARY_COLOUR))
            .jsonPath("$.neutralColour")
            .value(is(DEFAULT_NEUTRAL_COLOUR))
            .jsonPath("$.ligthBackgroundColour")
            .value(is(DEFAULT_LIGTH_BACKGROUND_COLOUR))
            .jsonPath("$.darkBackgroundColour")
            .value(is(DEFAULT_DARK_BACKGROUND_COLOUR));
    }

    @Test
    void getNonExistingAppColourPalette() {
        // Get the appColourPalette
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAppColourPalette() throws Exception {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appColourPalette
        AppColourPalette updatedAppColourPalette = appColourPaletteRepository.findById(appColourPalette.getId()).block();
        updatedAppColourPalette
            .description(UPDATED_DESCRIPTION)
            .primaryColour(UPDATED_PRIMARY_COLOUR)
            .secondaryColour(UPDATED_SECONDARY_COLOUR)
            .tertiaryColour(UPDATED_TERTIARY_COLOUR)
            .neutralColour(UPDATED_NEUTRAL_COLOUR)
            .ligthBackgroundColour(UPDATED_LIGTH_BACKGROUND_COLOUR)
            .darkBackgroundColour(UPDATED_DARK_BACKGROUND_COLOUR);
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(updatedAppColourPalette);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appColourPaletteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppColourPaletteToMatchAllProperties(updatedAppColourPalette);
    }

    @Test
    void putNonExistingAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, appColourPaletteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppColourPaletteWithPatch() throws Exception {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appColourPalette using partial update
        AppColourPalette partialUpdatedAppColourPalette = new AppColourPalette();
        partialUpdatedAppColourPalette.setId(appColourPalette.getId());

        partialUpdatedAppColourPalette
            .description(UPDATED_DESCRIPTION)
            .tertiaryColour(UPDATED_TERTIARY_COLOUR)
            .ligthBackgroundColour(UPDATED_LIGTH_BACKGROUND_COLOUR)
            .darkBackgroundColour(UPDATED_DARK_BACKGROUND_COLOUR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppColourPalette.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppColourPalette))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppColourPalette in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppColourPaletteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppColourPalette, appColourPalette),
            getPersistedAppColourPalette(appColourPalette)
        );
    }

    @Test
    void fullUpdateAppColourPaletteWithPatch() throws Exception {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appColourPalette using partial update
        AppColourPalette partialUpdatedAppColourPalette = new AppColourPalette();
        partialUpdatedAppColourPalette.setId(appColourPalette.getId());

        partialUpdatedAppColourPalette
            .description(UPDATED_DESCRIPTION)
            .primaryColour(UPDATED_PRIMARY_COLOUR)
            .secondaryColour(UPDATED_SECONDARY_COLOUR)
            .tertiaryColour(UPDATED_TERTIARY_COLOUR)
            .neutralColour(UPDATED_NEUTRAL_COLOUR)
            .ligthBackgroundColour(UPDATED_LIGTH_BACKGROUND_COLOUR)
            .darkBackgroundColour(UPDATED_DARK_BACKGROUND_COLOUR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAppColourPalette.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAppColourPalette))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AppColourPalette in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppColourPaletteUpdatableFieldsEquals(
            partialUpdatedAppColourPalette,
            getPersistedAppColourPalette(partialUpdatedAppColourPalette)
        );
    }

    @Test
    void patchNonExistingAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, appColourPaletteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAppColourPalette() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appColourPalette.setId(longCount.incrementAndGet());

        // Create the AppColourPalette
        AppColourPaletteDTO appColourPaletteDTO = appColourPaletteMapper.toDto(appColourPalette);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(appColourPaletteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AppColourPalette in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAppColourPalette() {
        // Initialize the database
        appColourPaletteRepository.save(appColourPalette).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appColourPalette
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, appColourPalette.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appColourPaletteRepository.count().block();
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

    protected AppColourPalette getPersistedAppColourPalette(AppColourPalette appColourPalette) {
        return appColourPaletteRepository.findById(appColourPalette.getId()).block();
    }

    protected void assertPersistedAppColourPaletteToMatchAllProperties(AppColourPalette expectedAppColourPalette) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppColourPaletteAllPropertiesEquals(expectedAppColourPalette, getPersistedAppColourPalette(expectedAppColourPalette));
        assertAppColourPaletteUpdatableFieldsEquals(expectedAppColourPalette, getPersistedAppColourPalette(expectedAppColourPalette));
    }

    protected void assertPersistedAppColourPaletteToMatchUpdatableProperties(AppColourPalette expectedAppColourPalette) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAppColourPaletteAllUpdatablePropertiesEquals(expectedAppColourPalette, getPersistedAppColourPalette(expectedAppColourPalette));
        assertAppColourPaletteUpdatableFieldsEquals(expectedAppColourPalette, getPersistedAppColourPalette(expectedAppColourPalette));
    }
}
