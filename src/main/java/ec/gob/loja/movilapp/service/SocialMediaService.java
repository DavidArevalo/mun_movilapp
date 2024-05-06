package ec.gob.loja.movilapp.service;

import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ec.gob.loja.movilapp.domain.SocialMedia}.
 */
public interface SocialMediaService {
    /**
     * Save a socialMedia.
     *
     * @param socialMediaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SocialMediaDTO> save(SocialMediaDTO socialMediaDTO);

    /**
     * Updates a socialMedia.
     *
     * @param socialMediaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SocialMediaDTO> update(SocialMediaDTO socialMediaDTO);

    /**
     * Partially updates a socialMedia.
     *
     * @param socialMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SocialMediaDTO> partialUpdate(SocialMediaDTO socialMediaDTO);

    /**
     * Get all the socialMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SocialMediaDTO> findAll(Pageable pageable);

    /**
     * Returns the number of socialMedias available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" socialMedia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SocialMediaDTO> findOne(Long id);

    /**
     * Delete the "id" socialMedia.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
