package ec.gob.loja.movilapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppBannerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppBannerDTO.class);
        AppBannerDTO appBannerDTO1 = new AppBannerDTO();
        appBannerDTO1.setId(1L);
        AppBannerDTO appBannerDTO2 = new AppBannerDTO();
        assertThat(appBannerDTO1).isNotEqualTo(appBannerDTO2);
        appBannerDTO2.setId(appBannerDTO1.getId());
        assertThat(appBannerDTO1).isEqualTo(appBannerDTO2);
        appBannerDTO2.setId(2L);
        assertThat(appBannerDTO1).isNotEqualTo(appBannerDTO2);
        appBannerDTO1.setId(null);
        assertThat(appBannerDTO1).isNotEqualTo(appBannerDTO2);
    }
}
