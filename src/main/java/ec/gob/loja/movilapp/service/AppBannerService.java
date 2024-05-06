package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.AppBanner}.
 */
public interface AppBannerService {
    /**
     * Save a appBanner.
     *
     * @param appBannerDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AppBannerDTO> save(AppBannerDTO appBannerDTO);

    /**
     * Updates a appBanner.
     *
     * @param appBannerDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AppBannerDTO> update(AppBannerDTO appBannerDTO);

    /**
     * Partially updates a appBanner.
     *
     * @param appBannerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AppBannerDTO> partialUpdate(AppBannerDTO appBannerDTO);

    /**
     * Get all the appBanners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AppBannerDTO> findAll(Pageable pageable);

    /**
     * Returns the number of appBanners available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" appBanner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AppBannerDTO> findOne(Long id);

    /**
     * Delete the "id" appBanner.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
