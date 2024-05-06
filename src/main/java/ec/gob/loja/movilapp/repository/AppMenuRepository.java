package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppMenu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AppMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppMenuRepository extends ReactiveCrudRepository<AppMenu, Long>, AppMenuRepositoryInternal {
    Flux<AppMenu> findAllBy(Pageable pageable);

    @Query("SELECT * FROM app_menu entity WHERE entity.application_id = :id")
    Flux<AppMenu> findByApplication(Long id);

    @Query("SELECT * FROM app_menu entity WHERE entity.application_id IS NULL")
    Flux<AppMenu> findAllWhereApplicationIsNull();

    @Override
    <S extends AppMenu> Mono<S> save(S entity);

    @Override
    Flux<AppMenu> findAll();

    @Override
    Mono<AppMenu> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppMenuRepositoryInternal {
    <S extends AppMenu> Mono<S> save(S entity);

    Flux<AppMenu> findAllBy(Pageable pageable);

    Flux<AppMenu> findAll();

    Mono<AppMenu> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppMenu> findAllBy(Pageable pageable, Criteria criteria);
}
