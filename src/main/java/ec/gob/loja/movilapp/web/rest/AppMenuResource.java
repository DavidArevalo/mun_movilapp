package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.AppMenuRepository;
import ec.gob.loja.movilapp.service.AppMenuService;
import ec.gob.loja.movilapp.service.dto.AppMenuDTO;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.AppMenu}.
 */
@RestController
@RequestMapping("/api/app-menus")
public class AppMenuResource {

    private final Logger log = LoggerFactory.getLogger(AppMenuResource.class);

    private static final String ENTITY_NAME = "movilappAppMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppMenuService appMenuService;

    private final AppMenuRepository appMenuRepository;

    public AppMenuResource(AppMenuService appMenuService, AppMenuRepository appMenuRepository) {
        this.appMenuService = appMenuService;
        this.appMenuRepository = appMenuRepository;
    }

    /**
     * {@code POST  /app-menus} : Create a new appMenu.
     *
     * @param appMenuDTO the appMenuDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appMenuDTO, or with status {@code 400 (Bad Request)} if the appMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AppMenuDTO>> createAppMenu(@Valid @RequestBody AppMenuDTO appMenuDTO) throws URISyntaxException {
        log.debug("REST request to save AppMenu : {}", appMenuDTO);
        if (appMenuDTO.getId() != null) {
            throw new BadRequestAlertException("A new appMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return appMenuService
            .save(appMenuDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/app-menus/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /app-menus/:id} : Updates an existing appMenu.
     *
     * @param id the id of the appMenuDTO to save.
     * @param appMenuDTO the appMenuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appMenuDTO,
     * or with status {@code 400 (Bad Request)} if the appMenuDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appMenuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AppMenuDTO>> updateAppMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppMenuDTO appMenuDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppMenu : {}, {}", id, appMenuDTO);
        if (appMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return appMenuService
                    .update(appMenuDTO)
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
     * {@code PATCH  /app-menus/:id} : Partial updates given fields of an existing appMenu, field will ignore if it is null
     *
     * @param id the id of the appMenuDTO to save.
     * @param appMenuDTO the appMenuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appMenuDTO,
     * or with status {@code 400 (Bad Request)} if the appMenuDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appMenuDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appMenuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AppMenuDTO>> partialUpdateAppMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppMenuDTO appMenuDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppMenu partially : {}, {}", id, appMenuDTO);
        if (appMenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMenuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return appMenuRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AppMenuDTO> result = appMenuService.partialUpdate(appMenuDTO);

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
     * {@code GET  /app-menus} : get all the appMenus.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appMenus in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AppMenuDTO>>> getAllAppMenus(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AppMenus");
        return appMenuService
            .countAll()
            .zipWith(appMenuService.findAll(pageable).collectList())
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
     * {@code GET  /app-menus/:id} : get the "id" appMenu.
     *
     * @param id the id of the appMenuDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appMenuDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AppMenuDTO>> getAppMenu(@PathVariable("id") Long id) {
        log.debug("REST request to get AppMenu : {}", id);
        Mono<AppMenuDTO> appMenuDTO = appMenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appMenuDTO);
    }

    /**
     * {@code DELETE  /app-menus/:id} : delete the "id" appMenu.
     *
     * @param id the id of the appMenuDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAppMenu(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppMenu : {}", id);
        return appMenuService
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
