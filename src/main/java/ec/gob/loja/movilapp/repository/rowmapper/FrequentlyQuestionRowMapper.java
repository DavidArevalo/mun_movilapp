package ec.gob.loja.movilapp.repository.rowmapper;

import ec.gob.loja.movilapp.domain.FrequentlyQuestion;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FrequentlyQuestion}, with proper type conversions.
 */
@Service
public class FrequentlyQuestionRowMapper implements BiFunction<Row, String, FrequentlyQuestion> {

    private final ColumnConverter converter;

    public FrequentlyQuestionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FrequentlyQuestion} stored in the database.
     */
    @Override
    public FrequentlyQuestion apply(Row row, String prefix) {
        FrequentlyQuestion entity = new FrequentlyQuestion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setQuestion(converter.fromRow(row, prefix + "_question", String.class));
        entity.setAnswer(converter.fromRow(row, prefix + "_answer", String.class));
        entity.setQuestionCategoryId(converter.fromRow(row, prefix + "_question_category_id", Long.class));
        entity.setApplicationId(converter.fromRow(row, prefix + "_application_id", Long.class));
        return entity;
    }
}
