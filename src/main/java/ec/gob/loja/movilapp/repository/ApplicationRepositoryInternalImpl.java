package ec.gob.loja.movilapp.repository;

import ec.gob.loja.movilapp.domain.AppBanner;
import ec.gob.loja.movilapp.domain.AppColourPalette;
import ec.gob.loja.movilapp.domain.Application;
import ec.gob.loja.movilapp.domain.SocialMedia;
import ec.gob.loja.movilapp.repository.rowmapper.ApplicationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Application entity.
 */
@SuppressWarnings("unused")
class ApplicationRepositoryInternalImpl extends SimpleR2dbcRepository<Application, Long> implements ApplicationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ApplicationRowMapper applicationMapper;

    private static final Table entityTable = Table.aliased("application", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable bannerLink = new EntityManager.LinkTable(
        "rel_application__banner",
        "application_id",
        "banner_id"
    );
    private static final EntityManager.LinkTable colourPaletteLink = new EntityManager.LinkTable(
        "rel_application__colour_palette",
        "application_id",
        "colour_palette_id"
    );
    private static final EntityManager.LinkTable socialMediaLink = new EntityManager.LinkTable(
        "rel_application__social_media",
        "application_id",
        "social_media_id"
    );

    public ApplicationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ApplicationRowMapper applicationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Application.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public Flux<Application> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Application> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ApplicationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Application.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Application> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Application> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Application> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Application> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Application> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Application process(Row row, RowMetadata metadata) {
        Application entity = applicationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Application> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Application> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(bannerLink, entity.getId(), entity.getBanners().stream().map(AppBanner::getId))
            .then();
        result = result.and(
            entityManager.updateLinkTable(
                colourPaletteLink,
                entity.getId(),
                entity.getColourPalettes().stream().map(AppColourPalette::getId)
            )
        );
        result = result.and(
            entityManager.updateLinkTable(socialMediaLink, entity.getId(), entity.getSocialMedias().stream().map(SocialMedia::getId))
        );
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager
            .deleteFromLinkTable(bannerLink, entityId)
            .and(entityManager.deleteFromLinkTable(colourPaletteLink, entityId))
            .and(entityManager.deleteFromLinkTable(socialMediaLink, entityId));
    }
}
