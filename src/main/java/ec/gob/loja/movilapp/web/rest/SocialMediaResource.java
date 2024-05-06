package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.SocialMediaRepository;
import ec.gob.loja.movilapp.service.SocialMediaService;
import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import ec.gob.loja.movilapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.SocialMedia}.
 */
@RestController
@RequestMapping("/api/social-medias")
public class SocialMediaResource {

    private final Logger log = LoggerFactory.getLogger(SocialMediaResource.class);

    private static final String ENTITY_NAME = "movilappSocialMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialMediaService socialMediaService;

    private final SocialMediaRepository socialMediaRepository;

    public SocialMediaResource(SocialMediaService socialMediaService, SocialMediaRepository socialMediaRepository) {
        this.socialMediaService = socialMediaService;
        this.socialMediaRepository = socialMediaRepository;
    }

    /**
     * {@code POST  /social-medias} : Create a new socialMedia.
     *
     * @param socialMediaDTO the socialMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialMediaDTO, or with status {@code 400 (Bad Request)} if the socialMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SocialMediaDTO>> createSocialMedia(@Valid @RequestBody SocialMediaDTO socialMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save SocialMedia : {}", socialMediaDTO);
        if (socialMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return socialMediaService
            .save(socialMediaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/social-medias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /social-medias/:id} : Updates an existing socialMedia.
     *
     * @param id the id of the socialMediaDTO to save.
     * @param socialMediaDTO the socialMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialMediaDTO,
     * or with status {@code 400 (Bad Request)} if the socialMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SocialMediaDTO>> updateSocialMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialMediaDTO socialMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialMedia : {}, {}", id, socialMediaDTO);
        if (socialMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return socialMediaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return socialMediaService
                    .update(socialMediaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /social-medias/:id} : Partial updates given fields of an existing socialMedia, field will ignore if it is null
     *
     * @param id the id of the socialMediaDTO to save.
     * @param socialMediaDTO the socialMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialMediaDTO,
     * or with status {@code 400 (Bad Request)} if the socialMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SocialMediaDTO>> partialUpdateSocialMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialMediaDTO socialMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialMedia partially : {}, {}", id, socialMediaDTO);
        if (socialMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return socialMediaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SocialMediaDTO> result = socialMediaService.partialUpdate(socialMediaDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /social-medias} : get all the socialMedias.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialMedias in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SocialMediaDTO>>> getAllSocialMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SocialMedias");
        return socialMediaService
            .countAll()
            .zipWith(socialMediaService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /social-medias/:id} : get the "id" socialMedia.
     *
     * @param id the id of the socialMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SocialMediaDTO>> getSocialMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get SocialMedia : {}", id);
        Mono<SocialMediaDTO> socialMediaDTO = socialMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialMediaDTO);
    }

    /**
     * {@code DELETE  /social-medias/:id} : delete the "id" socialMedia.
     *
     * @param id the id of the socialMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSocialMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete SocialMedia : {}", id);
        return socialMediaService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
