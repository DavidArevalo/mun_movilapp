package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.FrequentlyQuestionAsserts.*;
import static ec.gob.loja.movilapp.domain.FrequentlyQuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrequentlyQuestionMapperTest {

    private FrequentlyQuestionMapper frequentlyQuestionMapper;

    @BeforeEach
    void setUp() {
        frequentlyQuestionMapper = new FrequentlyQuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFrequentlyQuestionSample1();
        var actual = frequentlyQuestionMapper.toEntity(frequentlyQuestionMapper.toDto(expected));
        assertFrequentlyQuestionAllPropertiesEquals(expected, actual);
    }
}
