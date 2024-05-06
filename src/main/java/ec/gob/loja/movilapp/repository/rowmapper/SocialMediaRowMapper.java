package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.SocialMedia;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SocialMedia}, with proper type conversions.
 */
@Service
public class SocialMediaRowMapper implements BiFunction<Row, String, SocialMedia> {

    private final ColumnConverter converter;

    public SocialMediaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SocialMedia} stored in the database.
     */
    @Override
    public SocialMedia apply(Row row, String prefix) {
        SocialMedia entity = new SocialMedia();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setColour(converter.fromRow(row, prefix + "_colour", String.class));
        return entity;
    }
}
