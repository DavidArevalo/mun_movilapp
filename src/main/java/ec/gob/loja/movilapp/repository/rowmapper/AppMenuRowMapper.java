package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.AppMenu;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppMenu}, with proper type conversions.
 */
@Service
public class AppMenuRowMapper implements BiFunction<Row, String, AppMenu> {

    private final ColumnConverter converter;

    public AppMenuRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppMenu} stored in the database.
     */
    @Override
    public AppMenu apply(Row row, String prefix) {
        AppMenu entity = new AppMenu();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setPriority(converter.fromRow(row, prefix + "_priority", Integer.class));
        entity.setComponent(converter.fromRow(row, prefix + "_component", String.class));
        entity.setApplicationId(converter.fromRow(row, prefix + "_application_id", Long.class));
        return entity;
    }
}
