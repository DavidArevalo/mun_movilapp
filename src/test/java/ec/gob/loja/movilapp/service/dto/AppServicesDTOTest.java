package ec.gob.loja.movilapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppServicesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppServicesDTO.class);
        AppServicesDTO appServicesDTO1 = new AppServicesDTO();
        appServicesDTO1.setId(1L);
        AppServicesDTO appServicesDTO2 = new AppServicesDTO();
        assertThat(appServicesDTO1).isNotEqualTo(appServicesDTO2);
        appServicesDTO2.setId(appServicesDTO1.getId());
        assertThat(appServicesDTO1).isEqualTo(appServicesDTO2);
        appServicesDTO2.setId(2L);
        assertThat(appServicesDTO1).isNotEqualTo(appServicesDTO2);
        appServicesDTO1.setId(null);
        assertThat(appServicesDTO1).isNotEqualTo(appServicesDTO2);
    }
}
