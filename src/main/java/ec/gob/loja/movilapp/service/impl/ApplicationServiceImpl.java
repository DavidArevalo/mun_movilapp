package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.ApplicationRepository;
import ec.gob.loja.movilapp.service.ApplicationService;
import ec.gob.loja.movilapp.service.dto.ApplicationDTO;
import ec.gob.loja.movilapp.service.mapper.ApplicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.Application}.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public Mono<ApplicationDTO> save(ApplicationDTO applicationDTO) {
        log.debug("Request to save Application : {}", applicationDTO);
        return applicationRepository.save(applicationMapper.toEntity(applicationDTO)).map(applicationMapper::toDto);
    }

    @Override
    public Mono<ApplicationDTO> update(ApplicationDTO applicationDTO) {
        log.debug("Request to update Application : {}", applicationDTO);
        return applicationRepository.save(applicationMapper.toEntity(applicationDTO)).map(applicationMapper::toDto);
    }

    @Override
    public Mono<ApplicationDTO> partialUpdate(ApplicationDTO applicationDTO) {
        log.debug("Request to partially update Application : {}", applicationDTO);

        return applicationRepository
            .findById(applicationDTO.getId())
            .map(existingApplication -> {
                applicationMapper.partialUpdate(existingApplication, applicationDTO);

                return existingApplication;
            })
            .flatMap(applicationRepository::save)
            .map(applicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return applicationRepository.findAllBy(pageable).map(applicationMapper::toDto);
    }

    public Flux<ApplicationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return applicationRepository.findAllWithEagerRelationships(pageable).map(applicationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return applicationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ApplicationDTO> findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        return applicationRepository.findOneWithEagerRelationships(id).map(applicationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        return applicationRepository.deleteById(id);
    }
}
