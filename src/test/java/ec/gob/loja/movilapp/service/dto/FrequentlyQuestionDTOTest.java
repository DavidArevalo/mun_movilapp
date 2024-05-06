package ec.gob.loja.movilapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.loja.movilapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrequentlyQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrequentlyQuestionDTO.class);
        FrequentlyQuestionDTO frequentlyQuestionDTO1 = new FrequentlyQuestionDTO();
        frequentlyQuestionDTO1.setId(1L);
        FrequentlyQuestionDTO frequentlyQuestionDTO2 = new FrequentlyQuestionDTO();
        assertThat(frequentlyQuestionDTO1).isNotEqualTo(frequentlyQuestionDTO2);
        frequentlyQuestionDTO2.setId(frequentlyQuestionDTO1.getId());
        assertThat(frequentlyQuestionDTO1).isEqualTo(frequentlyQuestionDTO2);
        frequentlyQuestionDTO2.setId(2L);
        assertThat(frequentlyQuestionDTO1).isNotEqualTo(frequentlyQuestionDTO2);
        frequentlyQuestionDTO1.setId(null);
        assertThat(frequentlyQuestionDTO1).isNotEqualTo(frequentlyQuestionDTO2);
    }
}
