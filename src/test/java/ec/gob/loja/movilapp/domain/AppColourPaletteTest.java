package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.AppColourPaletteTestSamples.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppColourPaletteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppColourPalette.class);
        AppColourPalette appColourPalette1 = getAppColourPaletteSample1();
        AppColourPalette appColourPalette2 = new AppColourPalette();
        assertThat(appColourPalette1).isNotEqualTo(appColourPalette2);

        appColourPalette2.setId(appColourPalette1.getId());
        assertThat(appColourPalette1).isEqualTo(appColourPalette2);

        appColourPalette2 = getAppColourPaletteSample2();
        assertThat(appColourPalette1).isNotEqualTo(appColourPalette2);
    }

    @Test
    void applicationTest() throws Exception {
        AppColourPalette appColourPalette = getAppColourPaletteRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        appColourPalette.addApplication(applicationBack);
        assertThat(appColourPalette.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getColourPalettes()).containsOnly(appColourPalette);

        appColourPalette.removeApplication(applicationBack);
        assertThat(appColourPalette.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getColourPalettes()).doesNotContain(appColourPalette);

        appColourPalette.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(appColourPalette.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getColourPalettes()).containsOnly(appColourPalette);

        appColourPalette.setApplications(new HashSet<>());
        assertThat(appColourPalette.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getColourPalettes()).doesNotContain(appColourPalette);
    }
}
