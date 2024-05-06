package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Application getApplicationSample1() {
        return new Application().id(1L).code("code1").name("name1").urlAndroid("urlAndroid1").urlIos("urlIos1").description("description1");
    }

    public static Application getApplicationSample2() {
        return new Application().id(2L).code("code2").name("name2").urlAndroid("urlAndroid2").urlIos("urlIos2").description("description2");
    }

    public static Application getApplicationRandomSampleGenerator() {
        return new Application()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .urlAndroid(UUID.randomUUID().toString())
            .urlIos(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
