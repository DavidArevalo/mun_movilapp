package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.AppColourPalette}.
 */
public interface AppColourPaletteService {
    /**
     * Save a appColourPalette.
     *
     * @param appColourPaletteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AppColourPaletteDTO> save(AppColourPaletteDTO appColourPaletteDTO);

    /**
     * Updates a appColourPalette.
     *
     * @param appColourPaletteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AppColourPaletteDTO> update(AppColourPaletteDTO appColourPaletteDTO);

    /**
     * Partially updates a appColourPalette.
     *
     * @param appColourPaletteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AppColourPaletteDTO> partialUpdate(AppColourPaletteDTO appColourPaletteDTO);

    /**
     * Get all the appColourPalettes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AppColourPaletteDTO> findAll(Pageable pageable);

    /**
     * Returns the number of appColourPalettes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" appColourPalette.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AppColourPaletteDTO> findOne(Long id);

    /**
     * Delete the "id" appColourPalette.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
