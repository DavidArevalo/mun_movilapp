package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.AppServicesTestSamples.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppServicesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppServices.class);
        AppServices appServices1 = getAppServicesSample1();
        AppServices appServices2 = new AppServices();
        assertThat(appServices1).isNotEqualTo(appServices2);

        appServices2.setId(appServices1.getId());
        assertThat(appServices1).isEqualTo(appServices2);

        appServices2 = getAppServicesSample2();
        assertThat(appServices1).isNotEqualTo(appServices2);
    }

    @Test
    void applicationTest() throws Exception {
        AppServices appServices = getAppServicesRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        appServices.setApplication(applicationBack);
        assertThat(appServices.getApplication()).isEqualTo(applicationBack);

        appServices.application(null);
        assertThat(appServices.getApplication()).isNull();
    }
}
