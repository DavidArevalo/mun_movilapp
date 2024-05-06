package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.AppServicesRepository;
import ec.gob.loja.movilapp.service.AppServicesService;
import ec.gob.loja.movilapp.service.dto.AppServicesDTO;
import ec.gob.loja.movilapp.service.mapper.AppServicesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.AppServices}.
 */
@Service
@Transactional
public class AppServicesServiceImpl implements AppServicesService {

    private final Logger log = LoggerFactory.getLogger(AppServicesServiceImpl.class);

    private final AppServicesRepository appServicesRepository;

    private final AppServicesMapper appServicesMapper;

    public AppServicesServiceImpl(AppServicesRepository appServicesRepository, AppServicesMapper appServicesMapper) {
        this.appServicesRepository = appServicesRepository;
        this.appServicesMapper = appServicesMapper;
    }

    @Override
    public Mono<AppServicesDTO> save(AppServicesDTO appServicesDTO) {
        log.debug("Request to save AppServices : {}", appServicesDTO);
        return appServicesRepository.save(appServicesMapper.toEntity(appServicesDTO)).map(appServicesMapper::toDto);
    }

    @Override
    public Mono<AppServicesDTO> update(AppServicesDTO appServicesDTO) {
        log.debug("Request to update AppServices : {}", appServicesDTO);
        return appServicesRepository.save(appServicesMapper.toEntity(appServicesDTO)).map(appServicesMapper::toDto);
    }

    @Override
    public Mono<AppServicesDTO> partialUpdate(AppServicesDTO appServicesDTO) {
        log.debug("Request to partially update AppServices : {}", appServicesDTO);

        return appServicesRepository
            .findById(appServicesDTO.getId())
            .map(existingAppServices -> {
                appServicesMapper.partialUpdate(existingAppServices, appServicesDTO);

                return existingAppServices;
            })
            .flatMap(appServicesRepository::save)
            .map(appServicesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AppServicesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppServices");
        return appServicesRepository.findAllBy(pageable).map(appServicesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return appServicesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AppServicesDTO> findOne(Long id) {
        log.debug("Request to get AppServices : {}", id);
        return appServicesRepository.findById(id).map(appServicesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AppServices : {}", id);
        return appServicesRepository.deleteById(id);
    }
}
