package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.Application;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Application}, with proper type conversions.
 */
@Service
public class ApplicationRowMapper implements BiFunction<Row, String, Application> {

    private final ColumnConverter converter;

    public ApplicationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Application} stored in the database.
     */
    @Override
    public Application apply(Row row, String prefix) {
        Application entity = new Application();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setUrlAndroid(converter.fromRow(row, prefix + "_url_android", String.class));
        entity.setUrlIos(converter.fromRow(row, prefix + "_url_ios", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setVersion(converter.fromRow(row, prefix + "_version", Double.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        return entity;
    }
}
