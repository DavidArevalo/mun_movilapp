package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.SocialMedia;
import ec.gob.loja.movilapp.repository.rowmapper.SocialMediaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SocialMedia entity.
 */
@SuppressWarnings("unused")
class SocialMediaRepositoryInternalImpl extends SimpleR2dbcRepository<SocialMedia, Long> implements SocialMediaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SocialMediaRowMapper socialmediaMapper;

    private static final Table entityTable = Table.aliased("social_media", EntityManager.ENTITY_ALIAS);

    public SocialMediaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SocialMediaRowMapper socialmediaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SocialMedia.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.socialmediaMapper = socialmediaMapper;
    }

    @Override
    public Flux<SocialMedia> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SocialMedia> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SocialMediaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SocialMedia.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SocialMedia> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SocialMedia> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private SocialMedia process(Row row, RowMetadata metadata) {
        SocialMedia entity = socialmediaMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends SocialMedia> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
