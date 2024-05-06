package ec.gob.loja.movilapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AppBannerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("banner_image", table, columnPrefix + "_banner_image"));
        columns.add(Column.aliased("banner_image_content_type", table, columnPrefix + "_banner_image_content_type"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("init_date", table, columnPrefix + "_init_date"));
        columns.add(Column.aliased("end_date", table, columnPrefix + "_end_date"));
        columns.add(Column.aliased("init_time", table, columnPrefix + "_init_time"));
        columns.add(Column.aliased("end_time", table, columnPrefix + "_end_time"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));
        columns.add(Column.aliased("created_at", table, columnPrefix + "_created_at"));
        columns.add(Column.aliased("modification_date", table, columnPrefix + "_modification_date"));
        columns.add(Column.aliased("priority", table, columnPrefix + "_priority"));

        return columns;
    }
}
