package ec.gob.loja.movilapp.service.impl;

import ec.gob.loja.movilapp.repository.FrequentlyQuestionRepository;
import ec.gob.loja.movilapp.service.FrequentlyQuestionService;
import ec.gob.loja.movilapp.service.dto.FrequentlyQuestionDTO;
import ec.gob.loja.movilapp.service.mapper.FrequentlyQuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ec.gob.loja.movilapp.domain.FrequentlyQuestion}.
 */
@Service
@Transactional
public class FrequentlyQuestionServiceImpl implements FrequentlyQuestionService {

    private final Logger log = LoggerFactory.getLogger(FrequentlyQuestionServiceImpl.class);

    private final FrequentlyQuestionRepository frequentlyQuestionRepository;

    private final FrequentlyQuestionMapper frequentlyQuestionMapper;

    public FrequentlyQuestionServiceImpl(
        FrequentlyQuestionRepository frequentlyQuestionRepository,
        FrequentlyQuestionMapper frequentlyQuestionMapper
    ) {
        this.frequentlyQuestionRepository = frequentlyQuestionRepository;
        this.frequentlyQuestionMapper = frequentlyQuestionMapper;
    }

    @Override
    public Mono<FrequentlyQuestionDTO> save(FrequentlyQuestionDTO frequentlyQuestionDTO) {
        log.debug("Request to save FrequentlyQuestion : {}", frequentlyQuestionDTO);
        return frequentlyQuestionRepository
            .save(frequentlyQuestionMapper.toEntity(frequentlyQuestionDTO))
            .map(frequentlyQuestionMapper::toDto);
    }

    @Override
    public Mono<FrequentlyQuestionDTO> update(FrequentlyQuestionDTO frequentlyQuestionDTO) {
        log.debug("Request to update FrequentlyQuestion : {}", frequentlyQuestionDTO);
        return frequentlyQuestionRepository
            .save(frequentlyQuestionMapper.toEntity(frequentlyQuestionDTO))
            .map(frequentlyQuestionMapper::toDto);
    }

    @Override
    public Mono<FrequentlyQuestionDTO> partialUpdate(FrequentlyQuestionDTO frequentlyQuestionDTO) {
        log.debug("Request to partially update FrequentlyQuestion : {}", frequentlyQuestionDTO);

        return frequentlyQuestionRepository
            .findById(frequentlyQuestionDTO.getId())
            .map(existingFrequentlyQuestion -> {
                frequentlyQuestionMapper.partialUpdate(existingFrequentlyQuestion, frequentlyQuestionDTO);

                return existingFrequentlyQuestion;
            })
            .flatMap(frequentlyQuestionRepository::save)
            .map(frequentlyQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FrequentlyQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrequentlyQuestions");
        return frequentlyQuestionRepository.findAllBy(pageable).map(frequentlyQuestionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return frequentlyQuestionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FrequentlyQuestionDTO> findOne(Long id) {
        log.debug("Request to get FrequentlyQuestion : {}", id);
        return frequentlyQuestionRepository.findById(id).map(frequentlyQuestionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FrequentlyQuestion : {}", id);
        return frequentlyQuestionRepository.deleteById(id);
    }
}
