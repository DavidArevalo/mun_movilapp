package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.AppMenuRepository;
import ec.gob.loja.movilapp.service.AppMenuService;
import ec.gob.loja.movilapp.service.dto.AppMenuDTO;
import ec.gob.loja.movilapp.service.mapper.AppMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.AppMenu}.
 */
@Service
@Transactional
public class AppMenuServiceImpl implements AppMenuService {

    private final Logger log = LoggerFactory.getLogger(AppMenuServiceImpl.class);

    private final AppMenuRepository appMenuRepository;

    private final AppMenuMapper appMenuMapper;

    public AppMenuServiceImpl(AppMenuRepository appMenuRepository, AppMenuMapper appMenuMapper) {
        this.appMenuRepository = appMenuRepository;
        this.appMenuMapper = appMenuMapper;
    }

    @Override
    public Mono<AppMenuDTO> save(AppMenuDTO appMenuDTO) {
        log.debug("Request to save AppMenu : {}", appMenuDTO);
        return appMenuRepository.save(appMenuMapper.toEntity(appMenuDTO)).map(appMenuMapper::toDto);
    }

    @Override
    public Mono<AppMenuDTO> update(AppMenuDTO appMenuDTO) {
        log.debug("Request to update AppMenu : {}", appMenuDTO);
        return appMenuRepository.save(appMenuMapper.toEntity(appMenuDTO)).map(appMenuMapper::toDto);
    }

    @Override
    public Mono<AppMenuDTO> partialUpdate(AppMenuDTO appMenuDTO) {
        log.debug("Request to partially update AppMenu : {}", appMenuDTO);

        return appMenuRepository
            .findById(appMenuDTO.getId())
            .map(existingAppMenu -> {
                appMenuMapper.partialUpdate(existingAppMenu, appMenuDTO);

                return existingAppMenu;
            })
            .flatMap(appMenuRepository::save)
            .map(appMenuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AppMenuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppMenus");
        return appMenuRepository.findAllBy(pageable).map(appMenuMapper::toDto);
    }

    public Mono<Long> countAll() {
        return appMenuRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AppMenuDTO> findOne(Long id) {
        log.debug("Request to get AppMenu : {}", id);
        return appMenuRepository.findById(id).map(appMenuMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AppMenu : {}", id);
        return appMenuRepository.deleteById(id);
    }
}
