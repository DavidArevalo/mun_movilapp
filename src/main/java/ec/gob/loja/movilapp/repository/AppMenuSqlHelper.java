package ec.gob.loja.movilapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AppMenuSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));
        columns.add(Column.aliased("icon", table, columnPrefix + "_icon"));
        columns.add(Column.aliased("is_active", table, columnPrefix + "_is_active"));
        columns.add(Column.aliased("priority", table, columnPrefix + "_priority"));
        columns.add(Column.aliased("component", table, columnPrefix + "_component"));

        columns.add(Column.aliased("application_id", table, columnPrefix + "_application_id"));
        return columns;
    }
}
