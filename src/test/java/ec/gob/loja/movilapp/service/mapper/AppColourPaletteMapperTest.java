package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.AppColourPaletteAsserts.*;
import static ec.gob.loja.movilapp.domain.AppColourPaletteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppColourPaletteMapperTest {

    private AppColourPaletteMapper appColourPaletteMapper;

    @BeforeEach
    void setUp() {
        appColourPaletteMapper = new AppColourPaletteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppColourPaletteSample1();
        var actual = appColourPaletteMapper.toEntity(appColourPaletteMapper.toDto(expected));
        assertAppColourPaletteAllPropertiesEquals(expected, actual);
    }
}
