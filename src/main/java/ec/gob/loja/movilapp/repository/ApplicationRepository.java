package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.Application;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Application entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationRepository extends ReactiveCrudRepository<Application, Long>, ApplicationRepositoryInternal {
    Flux<Application> findAllBy(Pageable pageable);

    @Override
    Mono<Application> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Application> findAllWithEagerRelationships();

    @Override
    Flux<Application> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM application entity JOIN rel_application__banner joinTable ON entity.id = joinTable.banner_id WHERE joinTable.banner_id = :id"
    )
    Flux<Application> findByBanner(Long id);

    @Query(
        "SELECT entity.* FROM application entity JOIN rel_application__colour_palette joinTable ON entity.id = joinTable.colour_palette_id WHERE joinTable.colour_palette_id = :id"
    )
    Flux<Application> findByColourPalette(Long id);

    @Query(
        "SELECT entity.* FROM application entity JOIN rel_application__social_media joinTable ON entity.id = joinTable.social_media_id WHERE joinTable.social_media_id = :id"
    )
    Flux<Application> findBySocialMedia(Long id);

    @Override
    <S extends Application> Mono<S> save(S entity);

    @Override
    Flux<Application> findAll();

    @Override
    Mono<Application> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ApplicationRepositoryInternal {
    <S extends Application> Mono<S> save(S entity);

    Flux<Application> findAllBy(Pageable pageable);

    Flux<Application> findAll();

    Mono<Application> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Application> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Application> findOneWithEagerRelationships(Long id);

    Flux<Application> findAllWithEagerRelationships();

    Flux<Application> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
