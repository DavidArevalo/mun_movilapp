package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.AppBannerTestSamples.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppBannerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppBanner.class);
        AppBanner appBanner1 = getAppBannerSample1();
        AppBanner appBanner2 = new AppBanner();
        assertThat(appBanner1).isNotEqualTo(appBanner2);

        appBanner2.setId(appBanner1.getId());
        assertThat(appBanner1).isEqualTo(appBanner2);

        appBanner2 = getAppBannerSample2();
        assertThat(appBanner1).isNotEqualTo(appBanner2);
    }

    @Test
    void applicationTest() throws Exception {
        AppBanner appBanner = getAppBannerRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        appBanner.addApplication(applicationBack);
        assertThat(appBanner.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getBanners()).containsOnly(appBanner);

        appBanner.removeApplication(applicationBack);
        assertThat(appBanner.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getBanners()).doesNotContain(appBanner);

        appBanner.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(appBanner.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getBanners()).containsOnly(appBanner);

        appBanner.setApplications(new HashSet<>());
        assertThat(appBanner.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getBanners()).doesNotContain(appBanner);
    }
}
