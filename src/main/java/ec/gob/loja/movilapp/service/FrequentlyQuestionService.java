package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.FrequentlyQuestionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.FrequentlyQuestion}.
 */
public interface FrequentlyQuestionService {
    /**
     * Save a frequentlyQuestion.
     *
     * @param frequentlyQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FrequentlyQuestionDTO> save(FrequentlyQuestionDTO frequentlyQuestionDTO);

    /**
     * Updates a frequentlyQuestion.
     *
     * @param frequentlyQuestionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FrequentlyQuestionDTO> update(FrequentlyQuestionDTO frequentlyQuestionDTO);

    /**
     * Partially updates a frequentlyQuestion.
     *
     * @param frequentlyQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FrequentlyQuestionDTO> partialUpdate(FrequentlyQuestionDTO frequentlyQuestionDTO);

    /**
     * Get all the frequentlyQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FrequentlyQuestionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of frequentlyQuestions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" frequentlyQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FrequentlyQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" frequentlyQuestion.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
