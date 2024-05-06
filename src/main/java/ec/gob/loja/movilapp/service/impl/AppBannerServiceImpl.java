package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.AppBannerRepository;
import ec.gob.loja.movilapp.service.AppBannerService;
import ec.gob.loja.movilapp.service.dto.AppBannerDTO;
import ec.gob.loja.movilapp.service.mapper.AppBannerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.AppBanner}.
 */
@Service
@Transactional
public class AppBannerServiceImpl implements AppBannerService {

    private final Logger log = LoggerFactory.getLogger(AppBannerServiceImpl.class);

    private final AppBannerRepository appBannerRepository;

    private final AppBannerMapper appBannerMapper;

    public AppBannerServiceImpl(AppBannerRepository appBannerRepository, AppBannerMapper appBannerMapper) {
        this.appBannerRepository = appBannerRepository;
        this.appBannerMapper = appBannerMapper;
    }

    @Override
    public Mono<AppBannerDTO> save(AppBannerDTO appBannerDTO) {
        log.debug("Request to save AppBanner : {}", appBannerDTO);
        return appBannerRepository.save(appBannerMapper.toEntity(appBannerDTO)).map(appBannerMapper::toDto);
    }

    @Override
    public Mono<AppBannerDTO> update(AppBannerDTO appBannerDTO) {
        log.debug("Request to update AppBanner : {}", appBannerDTO);
        return appBannerRepository.save(appBannerMapper.toEntity(appBannerDTO)).map(appBannerMapper::toDto);
    }

    @Override
    public Mono<AppBannerDTO> partialUpdate(AppBannerDTO appBannerDTO) {
        log.debug("Request to partially update AppBanner : {}", appBannerDTO);

        return appBannerRepository
            .findById(appBannerDTO.getId())
            .map(existingAppBanner -> {
                appBannerMapper.partialUpdate(existingAppBanner, appBannerDTO);

                return existingAppBanner;
            })
            .flatMap(appBannerRepository::save)
            .map(appBannerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AppBannerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppBanners");
        return appBannerRepository.findAllBy(pageable).map(appBannerMapper::toDto);
    }

    public Mono<Long> countAll() {
        return appBannerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AppBannerDTO> findOne(Long id) {
        log.debug("Request to get AppBanner : {}", id);
        return appBannerRepository.findById(id).map(appBannerMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AppBanner : {}", id);
        return appBannerRepository.deleteById(id);
    }
}
