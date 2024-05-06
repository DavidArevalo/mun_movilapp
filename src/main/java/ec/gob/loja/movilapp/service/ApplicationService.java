package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.Application}.
 */
public interface ApplicationService {
    /**
     * Save a application.
     *
     * @param applicationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ApplicationDTO> save(ApplicationDTO applicationDTO);

    /**
     * Updates a application.
     *
     * @param applicationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ApplicationDTO> update(ApplicationDTO applicationDTO);

    /**
     * Partially updates a application.
     *
     * @param applicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ApplicationDTO> partialUpdate(ApplicationDTO applicationDTO);

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ApplicationDTO> findAll(Pageable pageable);

    /**
     * Get all the applications with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ApplicationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of applications available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" application.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" application.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
