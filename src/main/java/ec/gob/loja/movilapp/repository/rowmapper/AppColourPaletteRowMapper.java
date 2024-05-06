package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.AppColourPalette;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppColourPalette}, with proper type conversions.
 */
@Service
public class AppColourPaletteRowMapper implements BiFunction<Row, String, AppColourPalette> {

    private final ColumnConverter converter;

    public AppColourPaletteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppColourPalette} stored in the database.
     */
    @Override
    public AppColourPalette apply(Row row, String prefix) {
        AppColourPalette entity = new AppColourPalette();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPrimaryColour(converter.fromRow(row, prefix + "_primary_colour", String.class));
        entity.setSecondaryColour(converter.fromRow(row, prefix + "_secondary_colour", String.class));
        entity.setTertiaryColour(converter.fromRow(row, prefix + "_tertiary_colour", String.class));
        entity.setNeutralColour(converter.fromRow(row, prefix + "_neutral_colour", String.class));
        entity.setLigthBackgroundColour(converter.fromRow(row, prefix + "_ligth_background_colour", String.class));
        entity.setDarkBackgroundColour(converter.fromRow(row, prefix + "_dark_background_colour", String.class));
        return entity;
    }
}
