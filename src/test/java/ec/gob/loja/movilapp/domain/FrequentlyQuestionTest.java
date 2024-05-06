package ec.gob.loja.movilapp.domain;

import static ec.gob.loja.movilapp.domain.ApplicationTestSamples.*;
import static ec.gob.loja.movilapp.domain.FrequentlyQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrequentlyQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrequentlyQuestion.class);
        FrequentlyQuestion frequentlyQuestion1 = getFrequentlyQuestionSample1();
        FrequentlyQuestion frequentlyQuestion2 = new FrequentlyQuestion();
        assertThat(frequentlyQuestion1).isNotEqualTo(frequentlyQuestion2);

        frequentlyQuestion2.setId(frequentlyQuestion1.getId());
        assertThat(frequentlyQuestion1).isEqualTo(frequentlyQuestion2);

        frequentlyQuestion2 = getFrequentlyQuestionSample2();
        assertThat(frequentlyQuestion1).isNotEqualTo(frequentlyQuestion2);
    }

    @Test
    void applicationTest() throws Exception {
        FrequentlyQuestion frequentlyQuestion = getFrequentlyQuestionRandomSampleGenerator();
        Application applicationBack = getApplicationRandomSampleGenerator();

        frequentlyQuestion.setApplication(applicationBack);
        assertThat(frequentlyQuestion.getApplication()).isEqualTo(applicationBack);

        frequentlyQuestion.application(null);
        assertThat(frequentlyQuestion.getApplication()).isNull();
    }
}
