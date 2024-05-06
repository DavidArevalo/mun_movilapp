package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.AppMenuTestSamples.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppMenu.class);
        AppMenu appMenu1 = getAppMenuSample1();
        AppMenu appMenu2 = new AppMenu();
        assertThat(appMenu1).isNotEqualTo(appMenu2);

        appMenu2.setId(appMenu1.getId());
        assertThat(appMenu1).isEqualTo(appMenu2);

        appMenu2 = getAppMenuSample2();
        assertThat(appMenu1).isNotEqualTo(appMenu2);
    }

    @Test
    void applicationTest() throws Exception {
        AppMenu appMenu = getAppMenuRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        appMenu.setApplication(applicationBack);
        assertThat(appMenu.getApplication()).isEqualTo(applicationBack);

        appMenu.application(null);
        assertThat(appMenu.getApplication()).isNull();
    }
}
