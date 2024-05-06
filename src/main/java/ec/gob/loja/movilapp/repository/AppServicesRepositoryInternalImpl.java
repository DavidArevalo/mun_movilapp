package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppServices;
import ec.gob.loja.movilapp.repository.rowmapper.AppServicesRowMapper;
import ec.gob.loja.movilapp.repository.rowmapper.ApplicationRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the AppServices entity.
 */
@SuppressWarnings("unused")
class AppServicesRepositoryInternalImpl extends SimpleR2dbcRepository<AppServices, Long> implements AppServicesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ApplicationRowMapper applicationMapper;
    private final AppServicesRowMapper appservicesMapper;

    private static final Table entityTable = Table.aliased("app_services", EntityManager.ENTITY_ALIAS);
    private static final Table applicationTable = Table.aliased("application", "application");

    public AppServicesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ApplicationRowMapper applicationMapper,
        AppServicesRowMapper appservicesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AppServices.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.applicationMapper = applicationMapper;
        this.appservicesMapper = appservicesMapper;
    }

    @Override
    public Flux<AppServices> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AppServices> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AppServicesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ApplicationSqlHelper.getColumns(applicationTable, "application"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(applicationTable)
            .on(Column.create("application_id", entityTable))
            .equals(Column.create("id", applicationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AppServices.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AppServices> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AppServices> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AppServices process(Row row, RowMetadata metadata) {
        AppServices entity = appservicesMapper.apply(row, "e");
        entity.setApplication(applicationMapper.apply(row, "application"));
        return entity;
    }

    @Override
    public <S extends AppServices> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
