package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.AppColourPaletteRepository;
import ec.gob.loja.movilapp.service.AppColourPaletteService;
import ec.gob.loja.movilapp.service.dto.AppColourPaletteDTO;
import ec.gob.loja.movilapp.service.mapper.AppColourPaletteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.AppColourPalette}.
 */
@Service
@Transactional
public class AppColourPaletteServiceImpl implements AppColourPaletteService {

    private final Logger log = LoggerFactory.getLogger(AppColourPaletteServiceImpl.class);

    private final AppColourPaletteRepository appColourPaletteRepository;

    private final AppColourPaletteMapper appColourPaletteMapper;

    public AppColourPaletteServiceImpl(
        AppColourPaletteRepository appColourPaletteRepository,
        AppColourPaletteMapper appColourPaletteMapper
    ) {
        this.appColourPaletteRepository = appColourPaletteRepository;
        this.appColourPaletteMapper = appColourPaletteMapper;
    }

    @Override
    public Mono<AppColourPaletteDTO> save(AppColourPaletteDTO appColourPaletteDTO) {
        log.debug("Request to save AppColourPalette : {}", appColourPaletteDTO);
        return appColourPaletteRepository.save(appColourPaletteMapper.toEntity(appColourPaletteDTO)).map(appColourPaletteMapper::toDto);
    }

    @Override
    public Mono<AppColourPaletteDTO> update(AppColourPaletteDTO appColourPaletteDTO) {
        log.debug("Request to update AppColourPalette : {}", appColourPaletteDTO);
        return appColourPaletteRepository.save(appColourPaletteMapper.toEntity(appColourPaletteDTO)).map(appColourPaletteMapper::toDto);
    }

    @Override
    public Mono<AppColourPaletteDTO> partialUpdate(AppColourPaletteDTO appColourPaletteDTO) {
        log.debug("Request to partially update AppColourPalette : {}", appColourPaletteDTO);

        return appColourPaletteRepository
            .findById(appColourPaletteDTO.getId())
            .map(existingAppColourPalette -> {
                appColourPaletteMapper.partialUpdate(existingAppColourPalette, appColourPaletteDTO);

                return existingAppColourPalette;
            })
            .flatMap(appColourPaletteRepository::save)
            .map(appColourPaletteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AppColourPaletteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppColourPalettes");
        return appColourPaletteRepository.findAllBy(pageable).map(appColourPaletteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return appColourPaletteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AppColourPaletteDTO> findOne(Long id) {
        log.debug("Request to get AppColourPalette : {}", id);
        return appColourPaletteRepository.findById(id).map(appColourPaletteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AppColourPalette : {}", id);
        return appColourPaletteRepository.deleteById(id);
    }
}
