package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.SocialMediaAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.SocialMedia;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.repository.SocialMediaRepository;
import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import ec.gob.loja.movilapp.service.mapper.SocialMediaMapper;
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
 * Integration tests for the {@link SocialMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SocialMediaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_COLOUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private SocialMediaMapper socialMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SocialMedia socialMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialMedia createEntity(EntityManager em) {
        SocialMedia socialMedia = new SocialMedia().title(DEFAULT_TITLE).url(DEFAULT_URL).icon(DEFAULT_ICON).colour(DEFAULT_COLOUR);
        return socialMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialMedia createUpdatedEntity(EntityManager em) {
        SocialMedia socialMedia = new SocialMedia().title(UPDATED_TITLE).url(UPDATED_URL).icon(UPDATED_ICON).colour(UPDATED_COLOUR);
        return socialMedia;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SocialMedia.class).block();
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
        socialMedia = createEntity(em);
    }

    @Test
    void createSocialMedia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);
        var returnedSocialMediaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SocialMediaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the SocialMedia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSocialMedia = socialMediaMapper.toEntity(returnedSocialMediaDTO);
        assertSocialMediaUpdatableFieldsEquals(returnedSocialMedia, getPersistedSocialMedia(returnedSocialMedia));
    }

    @Test
    void createSocialMediaWithExistingId() throws Exception {
        // Create the SocialMedia with an existing ID
        socialMedia.setId(1L);
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialMedia.setTitle(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialMedia.setUrl(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIconIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialMedia.setIcon(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSocialMedias() {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        // Get all the socialMediaList
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
            .value(hasItem(socialMedia.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].colour")
            .value(hasItem(DEFAULT_COLOUR));
    }

    @Test
    void getSocialMedia() {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        // Get the socialMedia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, socialMedia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(socialMedia.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.colour")
            .value(is(DEFAULT_COLOUR));
    }

    @Test
    void getNonExistingSocialMedia() {
        // Get the socialMedia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSocialMedia() throws Exception {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialMedia
        SocialMedia updatedSocialMedia = socialMediaRepository.findById(socialMedia.getId()).block();
        updatedSocialMedia.title(UPDATED_TITLE).url(UPDATED_URL).icon(UPDATED_ICON).colour(UPDATED_COLOUR);
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(updatedSocialMedia);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, socialMediaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSocialMediaToMatchAllProperties(updatedSocialMedia);
    }

    @Test
    void putNonExistingSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, socialMediaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSocialMediaWithPatch() throws Exception {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialMedia using partial update
        SocialMedia partialUpdatedSocialMedia = new SocialMedia();
        partialUpdatedSocialMedia.setId(socialMedia.getId());

        partialUpdatedSocialMedia.title(UPDATED_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocialMedia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSocialMedia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialMediaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSocialMedia, socialMedia),
            getPersistedSocialMedia(socialMedia)
        );
    }

    @Test
    void fullUpdateSocialMediaWithPatch() throws Exception {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialMedia using partial update
        SocialMedia partialUpdatedSocialMedia = new SocialMedia();
        partialUpdatedSocialMedia.setId(socialMedia.getId());

        partialUpdatedSocialMedia.title(UPDATED_TITLE).url(UPDATED_URL).icon(UPDATED_ICON).colour(UPDATED_COLOUR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocialMedia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSocialMedia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SocialMedia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialMediaUpdatableFieldsEquals(partialUpdatedSocialMedia, getPersistedSocialMedia(partialUpdatedSocialMedia));
    }

    @Test
    void patchNonExistingSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, socialMediaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSocialMedia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(socialMediaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SocialMedia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSocialMedia() {
        // Initialize the database
        socialMediaRepository.save(socialMedia).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the socialMedia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, socialMedia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return socialMediaRepository.count().block();
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

    protected SocialMedia getPersistedSocialMedia(SocialMedia socialMedia) {
        return socialMediaRepository.findById(socialMedia.getId()).block();
    }

    protected void assertPersistedSocialMediaToMatchAllProperties(SocialMedia expectedSocialMedia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSocialMediaAllPropertiesEquals(expectedSocialMedia, getPersistedSocialMedia(expectedSocialMedia));
        assertSocialMediaUpdatableFieldsEquals(expectedSocialMedia, getPersistedSocialMedia(expectedSocialMedia));
    }

    protected void assertPersistedSocialMediaToMatchUpdatableProperties(SocialMedia expectedSocialMedia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSocialMediaAllUpdatablePropertiesEquals(expectedSocialMedia, getPersistedSocialMedia(expectedSocialMedia));
        assertSocialMediaUpdatableFieldsEquals(expectedSocialMedia, getPersistedSocialMedia(expectedSocialMedia));
    }
}
