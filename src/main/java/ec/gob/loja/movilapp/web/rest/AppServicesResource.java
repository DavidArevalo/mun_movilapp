package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.AppServicesRepository;
import ec.gob.loja.movilapp.service.AppServicesService;
import ec.gob.loja.movilapp.service.dto.AppServicesDTO;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.AppServices}.
 */
@RestController
@RequestMapping("/api/app-services")
public class AppServicesResource {

    private final Logger log = LoggerFactory.getLogger(AppServicesResource.class);

    private static final String ENTITY_NAME = "movilappAppServices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppServicesService appServicesService;

    private final AppServicesRepository appServicesRepository;

    public AppServicesResource(AppServicesService appServicesService, AppServicesRepository appServicesRepository) {
        this.appServicesService = appServicesService;
        this.appServicesRepository = appServicesRepository;
    }

    /**
     * {@code POST  /app-services} : Create a new appServices.
     *
     * @param appServicesDTO the appServicesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appServicesDTO, or with status {@code 400 (Bad Request)} if the appServices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AppServicesDTO>> createAppServices(@Valid @RequestBody AppServicesDTO appServicesDTO)
        throws URISyntaxException {
        log.debug("REST request to save AppServices : {}", appServicesDTO);
        if (appServicesDTO.getId() != null) {
            throw new BadRequestAlertException("A new appServices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appServicesService
            .save(appServicesDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/app-services/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-services/:id} : Updates an existing appServices.
     *
     * @param id the id of the appServicesDTO to save.
     * @param appServicesDTO the appServicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appServicesDTO,
     * or with status {@code 400 (Bad Request)} if the appServicesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appServicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppServicesDTO>> updateAppServices(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppServicesDTO appServicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppServices : {}, {}", id, appServicesDTO);
        if (appServicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appServicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appServicesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appServicesService
                    .update(appServicesDTO)
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
     * {@code PATCH  /app-services/:id} : Partial updates given fields of an existing appServices, field will ignore if it is null
     *
     * @param id the id of the appServicesDTO to save.
     * @param appServicesDTO the appServicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appServicesDTO,
     * or with status {@code 400 (Bad Request)} if the appServicesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appServicesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appServicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppServicesDTO>> partialUpdateAppServices(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppServicesDTO appServicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppServices partially : {}, {}", id, appServicesDTO);
        if (appServicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appServicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appServicesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppServicesDTO> result = appServicesService.partialUpdate(appServicesDTO);

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
     * {@code GET  /app-services} : get all the appServices.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appServices in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AppServicesDTO>>> getAllAppServices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AppServices");
        return appServicesService
            .countAll()
            .zipWith(appServicesService.findAll(pageable).collectList())
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
     * {@code GET  /app-services/:id} : get the "id" appServices.
     *
     * @param id the id of the appServicesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appServicesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppServicesDTO>> getAppServices(@PathVariable("id") Long id) {
        log.debug("REST request to get AppServices : {}", id);
        Mono<AppServicesDTO> appServicesDTO = appServicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appServicesDTO);
    }

    /**
     * {@code DELETE  /app-services/:id} : delete the "id" appServices.
     *
     * @param id the id of the appServicesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAppServices(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppServices : {}", id);
        return appServicesService
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
