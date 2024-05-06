package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.AppBannerRepository;
import ec.gob.loja.movilapp.service.AppBannerService;
import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.AppBanner}.
 */
@RestController
@RequestMapping("/api/app-banners")
public class AppBannerResource {

    private final Logger log = LoggerFactory.getLogger(AppBannerResource.class);

    private static final String ENTITY_NAME = "movilappAppBanner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppBannerService appBannerService;

    private final AppBannerRepository appBannerRepository;

    public AppBannerResource(AppBannerService appBannerService, AppBannerRepository appBannerRepository) {
        this.appBannerService = appBannerService;
        this.appBannerRepository = appBannerRepository;
    }

    /**
     * {@code POST  /app-banners} : Create a new appBanner.
     *
     * @param appBannerDTO the appBannerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appBannerDTO, or with status {@code 400 (Bad Request)} if the appBanner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AppBannerDTO>> createAppBanner(@Valid @RequestBody AppBannerDTO appBannerDTO) throws URISyntaxException {
        log.debug("REST request to save AppBanner : {}", appBannerDTO);
        if (appBannerDTO.getId() != null) {
            throw new BadRequestAlertException("A new appBanner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appBannerService
            .save(appBannerDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/app-banners/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-banners/:id} : Updates an existing appBanner.
     *
     * @param id the id of the appBannerDTO to save.
     * @param appBannerDTO the appBannerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appBannerDTO,
     * or with status {@code 400 (Bad Request)} if the appBannerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appBannerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppBannerDTO>> updateAppBanner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppBannerDTO appBannerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppBanner : {}, {}", id, appBannerDTO);
        if (appBannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appBannerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appBannerRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appBannerService
                    .update(appBannerDTO)
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
     * {@code PATCH  /app-banners/:id} : Partial updates given fields of an existing appBanner, field will ignore if it is null
     *
     * @param id the id of the appBannerDTO to save.
     * @param appBannerDTO the appBannerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appBannerDTO,
     * or with status {@code 400 (Bad Request)} if the appBannerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appBannerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appBannerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppBannerDTO>> partialUpdateAppBanner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppBannerDTO appBannerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppBanner partially : {}, {}", id, appBannerDTO);
        if (appBannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appBannerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appBannerRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppBannerDTO> result = appBannerService.partialUpdate(appBannerDTO);

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
     * {@code GET  /app-banners} : get all the appBanners.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appBanners in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AppBannerDTO>>> getAllAppBanners(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AppBanners");
        return appBannerService
            .countAll()
            .zipWith(appBannerService.findAll(pageable).collectList())
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
     * {@code GET  /app-banners/:id} : get the "id" appBanner.
     *
     * @param id the id of the appBannerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appBannerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppBannerDTO>> getAppBanner(@PathVariable("id") Long id) {
        log.debug("REST request to get AppBanner : {}", id);
        Mono<AppBannerDTO> appBannerDTO = appBannerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appBannerDTO);
    }

    /**
     * {@code DELETE  /app-banners/:id} : delete the "id" appBanner.
     *
     * @param id the id of the appBannerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAppBanner(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppBanner : {}", id);
        return appBannerService
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
