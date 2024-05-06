package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.AppServicesAsserts.*;
import static ec.gob.loja.movilapp.domain.AppServicesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppServicesMapperTest {

    private AppServicesMapper appServicesMapper;

    @BeforeEach
    void setUp() {
        appServicesMapper = new AppServicesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppServicesSample1();
        var actual = appServicesMapper.toEntity(appServicesMapper.toDto(expected));
        assertAppServicesAllPropertiesEquals(expected, actual);
    }
}
