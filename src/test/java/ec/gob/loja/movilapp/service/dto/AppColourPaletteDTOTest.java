package ec.gob.loja.movilapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppColourPaletteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppColourPaletteDTO.class);
        AppColourPaletteDTO appColourPaletteDTO1 = new AppColourPaletteDTO();
        appColourPaletteDTO1.setId(1L);
        AppColourPaletteDTO appColourPaletteDTO2 = new AppColourPaletteDTO();
        assertThat(appColourPaletteDTO1).isNotEqualTo(appColourPaletteDTO2);
        appColourPaletteDTO2.setId(appColourPaletteDTO1.getId());
        assertThat(appColourPaletteDTO1).isEqualTo(appColourPaletteDTO2);
        appColourPaletteDTO2.setId(2L);
        assertThat(appColourPaletteDTO1).isNotEqualTo(appColourPaletteDTO2);
        appColourPaletteDTO1.setId(null);
        assertThat(appColourPaletteDTO1).isNotEqualTo(appColourPaletteDTO2);
    }
}
