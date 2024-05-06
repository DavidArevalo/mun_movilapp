package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FrequentlyQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FrequentlyQuestion getFrequentlyQuestionSample1() {
        return new FrequentlyQuestion().id(1L).code("code1").question("question1").answer("answer1").questionCategoryId(1L);
    }

    public static FrequentlyQuestion getFrequentlyQuestionSample2() {
        return new FrequentlyQuestion().id(2L).code("code2").question("question2").answer("answer2").questionCategoryId(2L);
    }

    public static FrequentlyQuestion getFrequentlyQuestionRandomSampleGenerator() {
        return new FrequentlyQuestion()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .question(UUID.randomUUID().toString())
            .answer(UUID.randomUUID().toString())
            .questionCategoryId(longCount.incrementAndGet());
    }
}
