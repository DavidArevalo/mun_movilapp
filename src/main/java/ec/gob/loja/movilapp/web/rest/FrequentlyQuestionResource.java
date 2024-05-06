package ec.gob.loja.movilapp.web.rest;

import ec.gob.loja.movilapp.repository.FrequentlyQuestionRepository;
import ec.gob.loja.movilapp.service.FrequentlyQuestionService;
import ec.gob.loja.movilapp.service.dto.FrequentlyQuestionDTO;
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
 * REST controller for managing {@link ec.gob.loja.movilapp.domain.FrequentlyQuestion}.
 */
@RestController
@RequestMapping("/api/frequently-questions")
public class FrequentlyQuestionResource {

    private final Logger log = LoggerFactory.getLogger(FrequentlyQuestionResource.class);

    private static final String ENTITY_NAME = "movilappFrequentlyQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrequentlyQuestionService frequentlyQuestionService;

    private final FrequentlyQuestionRepository frequentlyQuestionRepository;

    public FrequentlyQuestionResource(
        FrequentlyQuestionService frequentlyQuestionService,
        FrequentlyQuestionRepository frequentlyQuestionRepository
    ) {
        this.frequentlyQuestionService = frequentlyQuestionService;
        this.frequentlyQuestionRepository = frequentlyQuestionRepository;
    }

    /**
     * {@code POST  /frequently-questions} : Create a new frequentlyQuestion.
     *
     * @param frequentlyQuestionDTO the frequentlyQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frequentlyQuestionDTO, or with status {@code 400 (Bad Request)} if the frequentlyQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FrequentlyQuestionDTO>> createFrequentlyQuestion(
        @Valid @RequestBody FrequentlyQuestionDTO frequentlyQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FrequentlyQuestion : {}", frequentlyQuestionDTO);
        if (frequentlyQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new frequentlyQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return frequentlyQuestionService
            .save(frequentlyQuestionDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/frequently-questions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /frequently-questions/:id} : Updates an existing frequentlyQuestion.
     *
     * @param id the id of the frequentlyQuestionDTO to save.
     * @param frequentlyQuestionDTO the frequentlyQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequentlyQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the frequentlyQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frequentlyQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FrequentlyQuestionDTO>> updateFrequentlyQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrequentlyQuestionDTO frequentlyQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FrequentlyQuestion : {}, {}", id, frequentlyQuestionDTO);
        if (frequentlyQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequentlyQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return frequentlyQuestionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return frequentlyQuestionService
                    .update(frequentlyQuestionDTO)
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
     * {@code PATCH  /frequently-questions/:id} : Partial updates given fields of an existing frequentlyQuestion, field will ignore if it is null
     *
     * @param id the id of the frequentlyQuestionDTO to save.
     * @param frequentlyQuestionDTO the frequentlyQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequentlyQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the frequentlyQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frequentlyQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frequentlyQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FrequentlyQuestionDTO>> partialUpdateFrequentlyQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrequentlyQuestionDTO frequentlyQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrequentlyQuestion partially : {}, {}", id, frequentlyQuestionDTO);
        if (frequentlyQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequentlyQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return frequentlyQuestionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FrequentlyQuestionDTO> result = frequentlyQuestionService.partialUpdate(frequentlyQuestionDTO);

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
     * {@code GET  /frequently-questions} : get all the frequentlyQuestions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frequentlyQuestions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FrequentlyQuestionDTO>>> getAllFrequentlyQuestions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of FrequentlyQuestions");
        return frequentlyQuestionService
            .countAll()
            .zipWith(frequentlyQuestionService.findAll(pageable).collectList())
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
     * {@code GET  /frequently-questions/:id} : get the "id" frequentlyQuestion.
     *
     * @param id the id of the frequentlyQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frequentlyQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FrequentlyQuestionDTO>> getFrequentlyQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get FrequentlyQuestion : {}", id);
        Mono<FrequentlyQuestionDTO> frequentlyQuestionDTO = frequentlyQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frequentlyQuestionDTO);
    }

    /**
     * {@code DELETE  /frequently-questions/:id} : delete the "id" frequentlyQuestion.
     *
     * @param id the id of the frequentlyQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFrequentlyQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete FrequentlyQuestion : {}", id);
        return frequentlyQuestionService
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
