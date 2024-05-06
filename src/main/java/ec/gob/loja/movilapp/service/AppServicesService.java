package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.AppServicesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.AppServices}.
 */
public interface AppServicesService {
    /**
     * Save a appServices.
     *
     * @param appServicesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AppServicesDTO> save(AppServicesDTO appServicesDTO);

    /**
     * Updates a appServices.
     *
     * @param appServicesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AppServicesDTO> update(AppServicesDTO appServicesDTO);

    /**
     * Partially updates a appServices.
     *
     * @param appServicesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AppServicesDTO> partialUpdate(AppServicesDTO appServicesDTO);

    /**
     * Get all the appServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AppServicesDTO> findAll(Pageable pageable);

    /**
     * Returns the number of appServices available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" appServices.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AppServicesDTO> findOne(Long id);

    /**
     * Delete the "id" appServices.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
