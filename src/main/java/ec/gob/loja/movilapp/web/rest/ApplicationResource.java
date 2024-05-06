package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.ApplicationRepository;
import ec.gob.loja.movilapp.service.ApplicationService;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.Application}.
 */
@RestController
@RequestMapping("/api/applications")
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    private static final String ENTITY_NAME = "movilappApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationService applicationService;

    private final ApplicationRepository applicationRepository;

    public ApplicationResource(ApplicationService applicationService, ApplicationRepository applicationRepository) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
    }

    /**
     * {@code POST  /applications} : Create a new application.
     *
     * @param applicationDTO the applicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationDTO, or with status {@code 400 (Bad Request)} if the application has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ApplicationDTO>> createApplication(@Valid @RequestBody ApplicationDTO applicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Application : {}", applicationDTO);
        if (applicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new application cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return applicationService
            .save(applicationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /applications/:id} : Updates an existing application.
     *
     * @param id the id of the applicationDTO to save.
     * @param applicationDTO the applicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationDTO,
     * or with status {@code 400 (Bad Request)} if the applicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApplicationDTO>> updateApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApplicationDTO applicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Application : {}, {}", id, applicationDTO);
        if (applicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return applicationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return applicationService
                    .update(applicationDTO)
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
     * {@code PATCH  /applications/:id} : Partial updates given fields of an existing application, field will ignore if it is null
     *
     * @param id the id of the applicationDTO to save.
     * @param applicationDTO the applicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationDTO,
     * or with status {@code 400 (Bad Request)} if the applicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the applicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ApplicationDTO>> partialUpdateApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApplicationDTO applicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Application partially : {}, {}", id, applicationDTO);
        if (applicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return applicationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ApplicationDTO> result = applicationService.partialUpdate(applicationDTO);

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
     * {@code GET  /applications} : get all the applications.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applications in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ApplicationDTO>>> getAllApplications(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Applications");
        return applicationService
            .countAll()
            .zipWith(applicationService.findAll(pageable).collectList())
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
     * {@code GET  /applications/:id} : get the "id" application.
     *
     * @param id the id of the applicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApplicationDTO>> getApplication(@PathVariable("id") Long id) {
        log.debug("REST request to get Application : {}", id);
        Mono<ApplicationDTO> applicationDTO = applicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationDTO);
    }

    /**
     * {@code DELETE  /applications/:id} : delete the "id" application.
     *
     * @param id the id of the applicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteApplication(@PathVariable("id") Long id) {
        log.debug("REST request to delete Application : {}", id);
        return applicationService
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
