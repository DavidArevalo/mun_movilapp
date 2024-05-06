package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.AppMenuAsserts.*;
import static ec.gob.loja.movilapp.domain.AppMenuTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppMenuMapperTest {

    private AppMenuMapper appMenuMapper;

    @BeforeEach
    void setUp() {
        appMenuMapper = new AppMenuMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppMenuSample1();
        var actual = appMenuMapper.toEntity(appMenuMapper.toDto(expected));
        assertAppMenuAllPropertiesEquals(expected, actual);
    }
}
