package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.FrequentlyQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FrequentlyQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrequentlyQuestionRepository
    extends ReactiveCrudRepository<FrequentlyQuestion, Long>, FrequentlyQuestionRepositoryInternal {
    Flux<FrequentlyQuestion> findAllBy(Pageable pageable);

    @Query("SELECT * FROM frequently_question entity WHERE entity.application_id = :id")
    Flux<FrequentlyQuestion> findByApplication(Long id);

    @Query("SELECT * FROM frequently_question entity WHERE entity.application_id IS NULL")
    Flux<FrequentlyQuestion> findAllWhereApplicationIsNull();

    @Override
    <S extends FrequentlyQuestion> Mono<S> save(S entity);

    @Override
    Flux<FrequentlyQuestion> findAll();

    @Override
    Mono<FrequentlyQuestion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FrequentlyQuestionRepositoryInternal {
    <S extends FrequentlyQuestion> Mono<S> save(S entity);

    Flux<FrequentlyQuestion> findAllBy(Pageable pageable);

    Flux<FrequentlyQuestion> findAll();

    Mono<FrequentlyQuestion> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FrequentlyQuestion> findAllBy(Pageable pageable, Criteria criteria);
}
