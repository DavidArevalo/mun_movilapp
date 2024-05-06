package ec.gob.loja.movilapp.web.rest;

import static ec.gob.loja.movilapp.domain.FrequentlyQuestionAsserts.*;
import static ec.gob.loja.movilapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.movilapp.IntegrationTest;
import ec.gob.loja.movilapp.domain.FrequentlyQuestion;
import ec.gob.loja.movilapp.repository.EntityManager;
import ec.gob.loja.movilapp.repository.FrequentlyQuestionRepository;
import ec.gob.loja.movilapp.service.dto.FrequentlyQuestionDTO;
import ec.gob.loja.movilapp.service.mapper.FrequentlyQuestionMapper;
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
 * Integration tests for the {@link FrequentlyQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FrequentlyQuestionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final Long DEFAULT_QUESTION_CATEGORY_ID = 1L;
    private static final Long UPDATED_QUESTION_CATEGORY_ID = 2L;

    private static final String ENTITY_API_URL = "/api/frequently-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FrequentlyQuestionRepository frequentlyQuestionRepository;

    @Autowired
    private FrequentlyQuestionMapper frequentlyQuestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FrequentlyQuestion frequentlyQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrequentlyQuestion createEntity(EntityManager em) {
        FrequentlyQuestion frequentlyQuestion = new FrequentlyQuestion()
            .code(DEFAULT_CODE)
            .question(DEFAULT_QUESTION)
            .answer(DEFAULT_ANSWER)
            .questionCategoryId(DEFAULT_QUESTION_CATEGORY_ID);
        return frequentlyQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrequentlyQuestion createUpdatedEntity(EntityManager em) {
        FrequentlyQuestion frequentlyQuestion = new FrequentlyQuestion()
            .code(UPDATED_CODE)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .questionCategoryId(UPDATED_QUESTION_CATEGORY_ID);
        return frequentlyQuestion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FrequentlyQuestion.class).block();
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
        frequentlyQuestion = createEntity(em);
    }

    @Test
    void createFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);
        var returnedFrequentlyQuestionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FrequentlyQuestionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FrequentlyQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFrequentlyQuestion = frequentlyQuestionMapper.toEntity(returnedFrequentlyQuestionDTO);
        assertFrequentlyQuestionUpdatableFieldsEquals(
            returnedFrequentlyQuestion,
            getPersistedFrequentlyQuestion(returnedFrequentlyQuestion)
        );
    }

    @Test
    void createFrequentlyQuestionWithExistingId() throws Exception {
        // Create the FrequentlyQuestion with an existing ID
        frequentlyQuestion.setId(1L);
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        frequentlyQuestion.setCode(null);

        // Create the FrequentlyQuestion, which fails.
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkQuestionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        frequentlyQuestion.setQuestion(null);

        // Create the FrequentlyQuestion, which fails.
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkQuestionCategoryIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        frequentlyQuestion.setQuestionCategoryId(null);

        // Create the FrequentlyQuestion, which fails.
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFrequentlyQuestions() {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        // Get all the frequentlyQuestionList
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
            .value(hasItem(frequentlyQuestion.getId().intValue()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].question")
            .value(hasItem(DEFAULT_QUESTION))
            .jsonPath("$.[*].answer")
            .value(hasItem(DEFAULT_ANSWER))
            .jsonPath("$.[*].questionCategoryId")
            .value(hasItem(DEFAULT_QUESTION_CATEGORY_ID.intValue()));
    }

    @Test
    void getFrequentlyQuestion() {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        // Get the frequentlyQuestion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, frequentlyQuestion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(frequentlyQuestion.getId().intValue()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.question")
            .value(is(DEFAULT_QUESTION))
            .jsonPath("$.answer")
            .value(is(DEFAULT_ANSWER))
            .jsonPath("$.questionCategoryId")
            .value(is(DEFAULT_QUESTION_CATEGORY_ID.intValue()));
    }

    @Test
    void getNonExistingFrequentlyQuestion() {
        // Get the frequentlyQuestion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFrequentlyQuestion() throws Exception {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentlyQuestion
        FrequentlyQuestion updatedFrequentlyQuestion = frequentlyQuestionRepository.findById(frequentlyQuestion.getId()).block();
        updatedFrequentlyQuestion
            .code(UPDATED_CODE)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .questionCategoryId(UPDATED_QUESTION_CATEGORY_ID);
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(updatedFrequentlyQuestion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, frequentlyQuestionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFrequentlyQuestionToMatchAllProperties(updatedFrequentlyQuestion);
    }

    @Test
    void putNonExistingFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, frequentlyQuestionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFrequentlyQuestionWithPatch() throws Exception {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentlyQuestion using partial update
        FrequentlyQuestion partialUpdatedFrequentlyQuestion = new FrequentlyQuestion();
        partialUpdatedFrequentlyQuestion.setId(frequentlyQuestion.getId());

        partialUpdatedFrequentlyQuestion.code(UPDATED_CODE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFrequentlyQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFrequentlyQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentlyQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFrequentlyQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFrequentlyQuestion, frequentlyQuestion),
            getPersistedFrequentlyQuestion(frequentlyQuestion)
        );
    }

    @Test
    void fullUpdateFrequentlyQuestionWithPatch() throws Exception {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentlyQuestion using partial update
        FrequentlyQuestion partialUpdatedFrequentlyQuestion = new FrequentlyQuestion();
        partialUpdatedFrequentlyQuestion.setId(frequentlyQuestion.getId());

        partialUpdatedFrequentlyQuestion
            .code(UPDATED_CODE)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER)
            .questionCategoryId(UPDATED_QUESTION_CATEGORY_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFrequentlyQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFrequentlyQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentlyQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFrequentlyQuestionUpdatableFieldsEquals(
            partialUpdatedFrequentlyQuestion,
            getPersistedFrequentlyQuestion(partialUpdatedFrequentlyQuestion)
        );
    }

    @Test
    void patchNonExistingFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, frequentlyQuestionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFrequentlyQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentlyQuestion.setId(longCount.incrementAndGet());

        // Create the FrequentlyQuestion
        FrequentlyQuestionDTO frequentlyQuestionDTO = frequentlyQuestionMapper.toDto(frequentlyQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentlyQuestionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FrequentlyQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFrequentlyQuestion() {
        // Initialize the database
        frequentlyQuestionRepository.save(frequentlyQuestion).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the frequentlyQuestion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, frequentlyQuestion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return frequentlyQuestionRepository.count().block();
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

    protected FrequentlyQuestion getPersistedFrequentlyQuestion(FrequentlyQuestion frequentlyQuestion) {
        return frequentlyQuestionRepository.findById(frequentlyQuestion.getId()).block();
    }

    protected void assertPersistedFrequentlyQuestionToMatchAllProperties(FrequentlyQuestion expectedFrequentlyQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFrequentlyQuestionAllPropertiesEquals(expectedFrequentlyQuestion, getPersistedFrequentlyQuestion(expectedFrequentlyQuestion));
        assertFrequentlyQuestionUpdatableFieldsEquals(
            expectedFrequentlyQuestion,
            getPersistedFrequentlyQuestion(expectedFrequentlyQuestion)
        );
    }

    protected void assertPersistedFrequentlyQuestionToMatchUpdatableProperties(FrequentlyQuestion expectedFrequentlyQuestion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFrequentlyQuestionAllUpdatablePropertiesEquals(expectedFrequentlyQuestion, getPersistedFrequentlyQuestion(expectedFrequentlyQuestion));
        assertFrequentlyQuestionUpdatableFieldsEquals(
            expectedFrequentlyQuestion,
            getPersistedFrequentlyQuestion(expectedFrequentlyQuestion)
        );
    }
}
