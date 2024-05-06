package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppServices;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AppServices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppServicesRepository extends ReactiveCrudRepository<AppServices, Long>, AppServicesRepositoryInternal {
    Flux<AppServices> findAllBy(Pageable pageable);

    @Query("SELECT * FROM app_services entity WHERE entity.application_id = :id")
    Flux<AppServices> findByApplication(Long id);

    @Query("SELECT * FROM app_services entity WHERE entity.application_id IS NULL")
    Flux<AppServices> findAllWhereApplicationIsNull();

    @Override
    <S extends AppServices> Mono<S> save(S entity);

    @Override
    Flux<AppServices> findAll();

    @Override
    Mono<AppServices> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppServicesRepositoryInternal {
    <S extends AppServices> Mono<S> save(S entity);

    Flux<AppServices> findAllBy(Pageable pageable);

    Flux<AppServices> findAll();

    Mono<AppServices> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppServices> findAllBy(Pageable pageable, Criteria criteria);
}
