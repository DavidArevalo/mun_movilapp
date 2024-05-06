package ec.gob.loja.movilapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppMenuDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppMenuDTO.class);
        AppMenuDTO appMenuDTO1 = new AppMenuDTO();
        appMenuDTO1.setId(1L);
        AppMenuDTO appMenuDTO2 = new AppMenuDTO();
        assertThat(appMenuDTO1).isNotEqualTo(appMenuDTO2);
        appMenuDTO2.setId(appMenuDTO1.getId());
        assertThat(appMenuDTO1).isEqualTo(appMenuDTO2);
        appMenuDTO2.setId(2L);
        assertThat(appMenuDTO1).isNotEqualTo(appMenuDTO2);
        appMenuDTO1.setId(null);
        assertThat(appMenuDTO1).isNotEqualTo(appMenuDTO2);
    }
}
