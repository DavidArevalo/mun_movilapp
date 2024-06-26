package ec.gob.loja.movilapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FrequentlyQuestionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("code", table, columnPrefix + "_code"));
        columns.add(Column.aliased("question", table, columnPrefix + "_question"));
        columns.add(Column.aliased("answer", table, columnPrefix + "_answer"));
        columns.add(Column.aliased("question_category_id", table, columnPrefix + "_question_category_id"));

        columns.add(Column.aliased("application_id", table, columnPrefix + "_application_id"));
        return columns;
    }
}
