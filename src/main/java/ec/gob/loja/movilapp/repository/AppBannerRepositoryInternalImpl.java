package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppBanner;
import ec.gob.loja.movilapp.repository.rowmapper.AppBannerRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the AppBanner entity.
 */
@SuppressWarnings("unused")
class AppBannerRepositoryInternalImpl extends SimpleR2dbcRepository<AppBanner, Long> implements AppBannerRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AppBannerRowMapper appbannerMapper;

    private static final Table entityTable = Table.aliased("app_banner", EntityManager.ENTITY_ALIAS);

    public AppBannerRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AppBannerRowMapper appbannerMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AppBanner.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.appbannerMapper = appbannerMapper;
    }

    @Override
    public Flux<AppBanner> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AppBanner> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AppBannerSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AppBanner.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AppBanner> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AppBanner> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AppBanner process(Row row, RowMetadata metadata) {
        AppBanner entity = appbannerMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends AppBanner> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
