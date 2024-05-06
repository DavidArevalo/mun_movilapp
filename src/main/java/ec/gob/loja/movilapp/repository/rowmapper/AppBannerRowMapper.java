package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.AppBanner;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppBanner}, with proper type conversions.
 */
@Service
public class AppBannerRowMapper implements BiFunction<Row, String, AppBanner> {

    private final ColumnConverter converter;

    public AppBannerRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppBanner} stored in the database.
     */
    @Override
    public AppBanner apply(Row row, String prefix) {
        AppBanner entity = new AppBanner();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setBannerImageContentType(converter.fromRow(row, prefix + "_banner_image_content_type", String.class));
        entity.setBannerImage(converter.fromRow(row, prefix + "_banner_image", byte[].class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setInitDate(converter.fromRow(row, prefix + "_init_date", LocalDate.class));
        entity.setEndDate(converter.fromRow(row, prefix + "_end_date", LocalDate.class));
        entity.setInitTime(converter.fromRow(row, prefix + "_init_time", ZonedDateTime.class));
        entity.setEndTime(converter.fromRow(row, prefix + "_end_time", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setUrl(converter.fromRow(row, prefix + "_url", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setModificationDate(converter.fromRow(row, prefix + "_modification_date", ZonedDateTime.class));
        entity.setPriority(converter.fromRow(row, prefix + "_priority", Integer.class));
        return entity;
    }
}
