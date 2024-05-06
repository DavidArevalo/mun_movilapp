package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppColourPalette;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AppColourPalette entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppColourPaletteRepository extends ReactiveCrudRepository<AppColourPalette, Long>, AppColourPaletteRepositoryInternal {
    Flux<AppColourPalette> findAllBy(Pageable pageable);

    @Override
    <S extends AppColourPalette> Mono<S> save(S entity);

    @Override
    Flux<AppColourPalette> findAll();

    @Override
    Mono<AppColourPalette> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AppColourPaletteRepositoryInternal {
    <S extends AppColourPalette> Mono<S> save(S entity);

    Flux<AppColourPalette> findAllBy(Pageable pageable);

    Flux<AppColourPalette> findAll();

    Mono<AppColourPalette> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AppColourPalette> findAllBy(Pageable pageable, Criteria criteria);
}
