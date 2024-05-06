package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.ApplicationAsserts.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationMapperTest {

    private ApplicationMapper applicationMapper;

    @BeforeEach
    void setUp() {
        applicationMapper = new ApplicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationSample1();
        var actual = applicationMapper.toEntity(applicationMapper.toDto(expected));
        assertApplicationAllPropertiesEquals(expected, actual);
    }
}
