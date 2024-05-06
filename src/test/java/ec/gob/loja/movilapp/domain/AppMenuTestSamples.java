package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppMenuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppMenu getAppMenuSample1() {
        return new AppMenu().id(1L).title("title1").url("url1").icon("icon1").priority(1).component("component1");
    }

    public static AppMenu getAppMenuSample2() {
        return new AppMenu().id(2L).title("title2").url("url2").icon("icon2").priority(2).component("component2");
    }

    public static AppMenu getAppMenuRandomSampleGenerator() {
        return new AppMenu()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .component(UUID.randomUUID().toString());
    }
}
