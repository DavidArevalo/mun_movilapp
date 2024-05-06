package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.AppMenuDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.AppMenu}.
 */
public interface AppMenuService {
    /**
     * Save a appMenu.
     *
     * @param appMenuDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AppMenuDTO> save(AppMenuDTO appMenuDTO);

    /**
     * Updates a appMenu.
     *
     * @param appMenuDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AppMenuDTO> update(AppMenuDTO appMenuDTO);

    /**
     * Partially updates a appMenu.
     *
     * @param appMenuDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AppMenuDTO> partialUpdate(AppMenuDTO appMenuDTO);

    /**
     * Get all the appMenus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AppMenuDTO> findAll(Pageable pageable);

    /**
     * Returns the number of appMenus available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" appMenu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AppMenuDTO> findOne(Long id);

    /**
     * Delete the "id" appMenu.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
