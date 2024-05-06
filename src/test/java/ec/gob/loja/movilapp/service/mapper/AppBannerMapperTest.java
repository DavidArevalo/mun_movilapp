package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.AppBannerAsserts.*;
import static ec.gob.loja.movilapp.domain.AppBannerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppBannerMapperTest {

    private AppBannerMapper appBannerMapper;

    @BeforeEach
    void setUp() {
        appBannerMapper = new AppBannerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppBannerSample1();
        var actual = appBannerMapper.toEntity(appBannerMapper.toDto(expected));
        assertAppBannerAllPropertiesEquals(expected, actual);
    }
}
