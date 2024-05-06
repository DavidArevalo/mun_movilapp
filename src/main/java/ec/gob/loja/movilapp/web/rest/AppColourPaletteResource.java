package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.AppColourPaletteRepository;
import ec.gob.loja.movilapp.service.AppColourPaletteService;
import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import ec.gob.loja.movilapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.AppColourPalette}.
 */
@RestController
@RequestMapping("/api/app-colour-palettes")
public class AppColourPaletteResource {

    private final Logger log = LoggerFactory.getLogger(AppColourPaletteResource.class);

    private static final String ENTITY_NAME = "movilappAppColourPalette";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppColourPaletteService appColourPaletteService;

    private final AppColourPaletteRepository appColourPaletteRepository;

    public AppColourPaletteResource(
        AppColourPaletteService appColourPaletteService,
        AppColourPaletteRepository appColourPaletteRepository
    ) {
        this.appColourPaletteService = appColourPaletteService;
        this.appColourPaletteRepository = appColourPaletteRepository;
    }

    /**
     * {@code POST  /app-colour-palettes} : Create a new appColourPalette.
     *
     * @param appColourPaletteDTO the appColourPaletteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appColourPaletteDTO, or with status {@code 400 (Bad Request)} if the appColourPalette has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AppColourPaletteDTO>> createAppColourPalette(@RequestBody AppColourPaletteDTO appColourPaletteDTO)
        throws URISyntaxException {
        log.debug("REST request to save AppColourPalette : {}", appColourPaletteDTO);
        if (appColourPaletteDTO.getId() != null) {
            throw new BadRequestAlertException("A new appColourPalette cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appColourPaletteService
            .save(appColourPaletteDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/app-colour-palettes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-colour-palettes/:id} : Updates an existing appColourPalette.
     *
     * @param id the id of the appColourPaletteDTO to save.
     * @param appColourPaletteDTO the appColourPaletteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appColourPaletteDTO,
     * or with status {@code 400 (Bad Request)} if the appColourPaletteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appColourPaletteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppColourPaletteDTO>> updateAppColourPalette(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppColourPaletteDTO appColourPaletteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppColourPalette : {}, {}", id, appColourPaletteDTO);
        if (appColourPaletteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appColourPaletteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appColourPaletteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appColourPaletteService
                    .update(appColourPaletteDTO)
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
     * {@code PATCH  /app-colour-palettes/:id} : Partial updates given fields of an existing appColourPalette, field will ignore if it is null
     *
     * @param id the id of the appColourPaletteDTO to save.
     * @param appColourPaletteDTO the appColourPaletteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appColourPaletteDTO,
     * or with status {@code 400 (Bad Request)} if the appColourPaletteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appColourPaletteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appColourPaletteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppColourPaletteDTO>> partialUpdateAppColourPalette(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppColourPaletteDTO appColourPaletteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppColourPalette partially : {}, {}", id, appColourPaletteDTO);
        if (appColourPaletteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appColourPaletteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appColourPaletteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppColourPaletteDTO> result = appColourPaletteService.partialUpdate(appColourPaletteDTO);

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
     * {@code GET  /app-colour-palettes} : get all the appColourPalettes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appColourPalettes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AppColourPaletteDTO>>> getAllAppColourPalettes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AppColourPalettes");
        return appColourPaletteService
            .countAll()
            .zipWith(appColourPaletteService.findAll(pageable).collectList())
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
     * {@code GET  /app-colour-palettes/:id} : get the "id" appColourPalette.
     *
     * @param id the id of the appColourPaletteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appColourPaletteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppColourPaletteDTO>> getAppColourPalette(@PathVariable("id") Long id) {
        log.debug("REST request to get AppColourPalette : {}", id);
        Mono<AppColourPaletteDTO> appColourPaletteDTO = appColourPaletteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appColourPaletteDTO);
    }

    /**
     * {@code DELETE  /app-colour-palettes/:id} : delete the "id" appColourPalette.
     *
     * @param id the id of the appColourPaletteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAppColourPalette(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppColourPalette : {}", id);
        return appColourPaletteService
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
