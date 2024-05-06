package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.SocialMedia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SocialMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialMediaRepository extends ReactiveCrudRepository<SocialMedia, Long>, SocialMediaRepositoryInternal {
    Flux<SocialMedia> findAllBy(Pageable pageable);

    @Override
    <S extends SocialMedia> Mono<S> save(S entity);

    @Override
    Flux<SocialMedia> findAll();

    @Override
    Mono<SocialMedia> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SocialMediaRepositoryInternal {
    <S extends SocialMedia> Mono<S> save(S entity);

    Flux<SocialMedia> findAllBy(Pageable pageable);

    Flux<SocialMedia> findAll();

    Mono<SocialMedia> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SocialMedia> findAllBy(Pageable pageable, Criteria criteria);
}
