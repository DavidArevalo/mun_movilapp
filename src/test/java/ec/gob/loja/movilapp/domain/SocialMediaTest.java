package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static ec.gob.loja.movilapp.domain.SocialMediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SocialMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialMedia.class);
        SocialMedia socialMedia1 = getSocialMediaSample1();
        SocialMedia socialMedia2 = new SocialMedia();
        assertThat(socialMedia1).isNotEqualTo(socialMedia2);

        socialMedia2.setId(socialMedia1.getId());
        assertThat(socialMedia1).isEqualTo(socialMedia2);

        socialMedia2 = getSocialMediaSample2();
        assertThat(socialMedia1).isNotEqualTo(socialMedia2);
    }

    @Test
    void applicationTest() throws Exception {
        SocialMedia socialMedia = getSocialMediaRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        socialMedia.addApplication(applicationBack);
        assertThat(socialMedia.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getSocialMedias()).containsOnly(socialMedia);

        socialMedia.removeApplication(applicationBack);
        assertThat(socialMedia.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getSocialMedias()).doesNotContain(socialMedia);

        socialMedia.applications(new HashSet<>(Set.of(applicationBack)));
        assertThat(socialMedia.getApplications()).containsOnly(applicationBack);
        assertThat(applicationBack.getSocialMedias()).containsOnly(socialMedia);

        socialMedia.setApplications(new HashSet<>());
        assertThat(socialMedia.getApplications()).doesNotContain(applicationBack);
        assertThat(applicationBack.getSocialMedias()).doesNotContain(socialMedia);
    }
}
