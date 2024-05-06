package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppServicesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppServices getAppServicesSample1() {
        return new AppServices().id(1L).title("title1").url("url1").icon("icon1").backgroundCard("backgroundCard1").priority(1);
    }

    public static AppServices getAppServicesSample2() {
        return new AppServices().id(2L).title("title2").url("url2").icon("icon2").backgroundCard("backgroundCard2").priority(2);
    }

    public static AppServices getAppServicesRandomSampleGenerator() {
        return new AppServices()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .backgroundCard(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet());
    }
}
