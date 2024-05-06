package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.SocialMediaRepository;
import ec.gob.loja.movilapp.service.SocialMediaService;
import ec.gob.loja.movilapp.service.dto.SocialMediaDTO;
import ec.gob.loja.movilapp.service.mapper.SocialMediaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.SocialMedia}.
 */
@Service
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {

    private final Logger log = LoggerFactory.getLogger(SocialMediaServiceImpl.class);

    private final SocialMediaRepository socialMediaRepository;

    private final SocialMediaMapper socialMediaMapper;

    public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository, SocialMediaMapper socialMediaMapper) {
        this.socialMediaRepository = socialMediaRepository;
        this.socialMediaMapper = socialMediaMapper;
    }

    @Override
    public Mono<SocialMediaDTO> save(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to save SocialMedia : {}", socialMediaDTO);
        return socialMediaRepository.save(socialMediaMapper.toEntity(socialMediaDTO)).map(socialMediaMapper::toDto);
    }

    @Override
    public Mono<SocialMediaDTO> update(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to update SocialMedia : {}", socialMediaDTO);
        return socialMediaRepository.save(socialMediaMapper.toEntity(socialMediaDTO)).map(socialMediaMapper::toDto);
    }

    @Override
    public Mono<SocialMediaDTO> partialUpdate(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to partially update SocialMedia : {}", socialMediaDTO);

        return socialMediaRepository
            .findById(socialMediaDTO.getId())
            .map(existingSocialMedia -> {
                socialMediaMapper.partialUpdate(existingSocialMedia, socialMediaDTO);

                return existingSocialMedia;
            })
            .flatMap(socialMediaRepository::save)
            .map(socialMediaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SocialMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SocialMedias");
        return socialMediaRepository.findAllBy(pageable).map(socialMediaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return socialMediaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SocialMediaDTO> findOne(Long id) {
        log.debug("Request to get SocialMedia : {}", id);
        return socialMediaRepository.findById(id).map(socialMediaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SocialMedia : {}", id);
        return socialMediaRepository.deleteById(id);
    }
}
