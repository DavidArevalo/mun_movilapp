package ec.gob.loja.movilapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AppColourPaletteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("primary_colour", table, columnPrefix + "_primary_colour"));
        columns.add(Column.aliased("secondary_colour", table, columnPrefix + "_secondary_colour"));
        columns.add(Column.aliased("tertiary_colour", table, columnPrefix + "_tertiary_colour"));
        columns.add(Column.aliased("neutral_colour", table, columnPrefix + "_neutral_colour"));
        columns.add(Column.aliased("ligth_background_colour", table, columnPrefix + "_ligth_background_colour"));
        columns.add(Column.aliased("dark_background_colour", table, columnPrefix + "_dark_background_colour"));

        return columns;
    }
}
