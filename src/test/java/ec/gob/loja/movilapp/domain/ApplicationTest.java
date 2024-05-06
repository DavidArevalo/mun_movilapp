package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.AppBannerTestSamples.*;
import static ec.gob.loja.movilapp.domain.AppColourPaletteTestSamples.*;
import static ec.gob.loja.movilapp.domain.AppMenuTestSamples.*;
import static ec.gob.loja.movilapp.domain.AppServicesTestSamples.*;
import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static ec.gob.loja.movilapp.domain.FrequentlyQuestionTestSamples.*;
import static ec.gob.loja.movilapp.domain.SocialMediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Application.class);
        Application application1 = getApplicationSample1();
        Application application2 = new Application();
        assertThat(application1).isNotEqualTo(application2);

        application2.setId(application1.getId());
        assertThat(application1).isEqualTo(application2);

        application2 = getApplicationSample2();
        assertThat(application1).isNotEqualTo(application2);
    }

    @Test
    void serviceTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        AppServices appServicesBack = getAppServicesRandomSampleGenerator();

        application.addService(appServicesBack);
        assertThat(application.getServices()).containsOnly(appServicesBack);
        assertThat(appServicesBack.getApplication()).isEqualTo(application);

        application.removeService(appServicesBack);
        assertThat(application.getServices()).doesNotContain(appServicesBack);
        assertThat(appServicesBack.getApplication()).isNull();

        application.services(new HashSet<>(Set.of(appServicesBack)));
        assertThat(application.getServices()).containsOnly(appServicesBack);
        assertThat(appServicesBack.getApplication()).isEqualTo(application);

        application.setServices(new HashSet<>());
        assertThat(application.getServices()).doesNotContain(appServicesBack);
        assertThat(appServicesBack.getApplication()).isNull();
    }

    @Test
    void menuTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        AppMenu appMenuBack = getAppMenuRandomSampleGenerator();

        application.addMenu(appMenuBack);
        assertThat(application.getMenus()).containsOnly(appMenuBack);
        assertThat(appMenuBack.getApplication()).isEqualTo(application);

        application.removeMenu(appMenuBack);
        assertThat(application.getMenus()).doesNotContain(appMenuBack);
        assertThat(appMenuBack.getApplication()).isNull();

        application.menus(new HashSet<>(Set.of(appMenuBack)));
        assertThat(application.getMenus()).containsOnly(appMenuBack);
        assertThat(appMenuBack.getApplication()).isEqualTo(application);

        application.setMenus(new HashSet<>());
        assertThat(application.getMenus()).doesNotContain(appMenuBack);
        assertThat(appMenuBack.getApplication()).isNull();
    }

    @Test
    void frequentlyQuestionTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        FrequentlyQuestion frequentlyQuestionBack = getFrequentlyQuestionRandomSampleGenerator();

        application.addFrequentlyQuestion(frequentlyQuestionBack);
        assertThat(application.getFrequentlyQuestions()).containsOnly(frequentlyQuestionBack);
        assertThat(frequentlyQuestionBack.getApplication()).isEqualTo(application);

        application.removeFrequentlyQuestion(frequentlyQuestionBack);
        assertThat(application.getFrequentlyQuestions()).doesNotContain(frequentlyQuestionBack);
        assertThat(frequentlyQuestionBack.getApplication()).isNull();

        application.frequentlyQuestions(new HashSet<>(Set.of(frequentlyQuestionBack)));
        assertThat(application.getFrequentlyQuestions()).containsOnly(frequentlyQuestionBack);
        assertThat(frequentlyQuestionBack.getApplication()).isEqualTo(application);

        application.setFrequentlyQuestions(new HashSet<>());
        assertThat(application.getFrequentlyQuestions()).doesNotContain(frequentlyQuestionBack);
        assertThat(frequentlyQuestionBack.getApplication()).isNull();
    }

    @Test
    void bannerTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        AppBanner appBannerBack = getAppBannerRandomSampleGenerator();

        application.addBanner(appBannerBack);
        assertThat(application.getBanners()).containsOnly(appBannerBack);

        application.removeBanner(appBannerBack);
        assertThat(application.getBanners()).doesNotContain(appBannerBack);

        application.banners(new HashSet<>(Set.of(appBannerBack)));
        assertThat(application.getBanners()).containsOnly(appBannerBack);

        application.setBanners(new HashSet<>());
        assertThat(application.getBanners()).doesNotContain(appBannerBack);
    }

    @Test
    void colourPaletteTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        AppColourPalette appColourPaletteBack = getAppColourPaletteRandomSampleGenerator();

        application.addColourPalette(appColourPaletteBack);
        assertThat(application.getColourPalettes()).containsOnly(appColourPaletteBack);

        application.removeColourPalette(appColourPaletteBack);
        assertThat(application.getColourPalettes()).doesNotContain(appColourPaletteBack);

        application.colourPalettes(new HashSet<>(Set.of(appColourPaletteBack)));
        assertThat(application.getColourPalettes()).containsOnly(appColourPaletteBack);

        application.setColourPalettes(new HashSet<>());
        assertThat(application.getColourPalettes()).doesNotContain(appColourPaletteBack);
    }

    @Test
    void socialMediaTest() throws Exception {
        Application application = getApplicationRandomSampleGenerator();
        SocialMedia socialMediaBack = getSocialMediaRandomSampleGenerator();

        application.addSocialMedia(socialMediaBack);
        assertThat(application.getSocialMedias()).containsOnly(socialMediaBack);

        application.removeSocialMedia(socialMediaBack);
        assertThat(application.getSocialMedias()).doesNotContain(socialMediaBack);

        application.socialMedias(new HashSet<>(Set.of(socialMediaBack)));
        assertThat(application.getSocialMedias()).containsOnly(socialMediaBack);

        application.setSocialMedias(new HashSet<>());
        assertThat(application.getSocialMedias()).doesNotContain(socialMediaBack);
    }
}
