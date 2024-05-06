package ec.gob.loja.movilapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.gob.loja.movilapp.domain.FrequentlyQuestion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrequentlyQuestionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String code;

    @NotNull(message = "must not be null")
    private String question;

    private String answer;

    @NotNull(message = "must not be null")
    private Long questionCategoryId;

    private ApplicationDTO application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(Long questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrequentlyQuestionDTO)) {
            return false;
        }

        FrequentlyQuestionDTO frequentlyQuestionDTO = (FrequentlyQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, frequentlyQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrequentlyQuestionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", question='" + getQuestion() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", questionCategoryId=" + getQuestionCategoryId() +
            ", application=" + getApplication() +
            "}";
    }
}
