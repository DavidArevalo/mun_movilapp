package ec.gob.loja.movilapp.service.mapper;

import static ec.gob.loja.movilapp.domain.SocialMediaAsserts.*;
import static ec.gob.loja.movilapp.domain.SocialMediaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialMediaMapperTest {

    private SocialMediaMapper socialMediaMapper;

    @BeforeEach
    void setUp() {
        socialMediaMapper = new SocialMediaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSocialMediaSample1();
        var actual = socialMediaMapper.toEntity(socialMediaMapper.toDto(expected));
        assertSocialMediaAllPropertiesEquals(expected, actual);
    }
}
