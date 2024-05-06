package ec.gob.loja.movilapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FrequentlyQuestion.
 */
@Table("frequently_question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FrequentlyQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("question")
    private String question;

    @Column("answer")
    private String answer;

    @NotNull(message = "must not be null")
    @Column("question_category_id")
    private Long questionCategoryId;

    @Transient
    @JsonIgnoreProperties(
        value = { "services", "menus", "frequentlyQuestions", "banners", "colourPalettes", "socialMedias" },
        allowSetters = true
    )
    private Application application;

    @Column("application_id")
    private Long applicationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FrequentlyQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public FrequentlyQuestion code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestion() {
        return this.question;
    }

    public FrequentlyQuestion question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public FrequentlyQuestion answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getQuestionCategoryId() {
        return this.questionCategoryId;
    }

    public FrequentlyQuestion questionCategoryId(Long questionCategoryId) {
        this.setQuestionCategoryId(questionCategoryId);
        return this;
    }

    public void setQuestionCategoryId(Long questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
        this.applicationId = application != null ? application.getId() : null;
    }

    public FrequentlyQuestion application(Application application) {
        this.setApplication(application);
        return this;
    }

    public Long getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(Long application) {
        this.applicationId = application;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FrequentlyQuestion)) {
            return false;
        }
        return getId() != null && getId().equals(((FrequentlyQuestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FrequentlyQuestion{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", question='" + getQuestion() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", questionCategoryId=" + getQuestionCategoryId() +
            "}";
    }
}
