package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppMenu;
import ec.gob.loja.movilapp.repository.rowmapper.AppMenuRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the AppMenu entity.
 */
@SuppressWarnings("unused")
class AppMenuRepositoryInternalImpl extends SimpleR2dbcRepository<AppMenu, Long> implements AppMenuRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ApplicationRowMapper applicationMapper;
    private final AppMenuRowMapper appmenuMapper;

    private static final Table entityTable = Table.aliased("app_menu", EntityManager.ENTITY_ALIAS);
    private static final Table applicationTable = Table.aliased("application", "application");

    public AppMenuRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ApplicationRowMapper applicationMapper,
        AppMenuRowMapper appmenuMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AppMenu.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.applicationMapper = applicationMapper;
        this.appmenuMapper = appmenuMapper;
    }

    @Override
    public Flux<AppMenu> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AppMenu> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AppMenuSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ApplicationSqlHelper.getColumns(applicationTable, "application"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(applicationTable)
            .on(Column.create("application_id", entityTable))
            .equals(Column.create("id", applicationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AppMenu.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AppMenu> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AppMenu> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private AppMenu process(Row row, RowMetadata metadata) {
        AppMenu entity = appmenuMapper.apply(row, "e");
        entity.setApplication(applicationMapper.apply(row, "application"));
        return entity;
    }

    @Override
    public <S extends AppMenu> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
