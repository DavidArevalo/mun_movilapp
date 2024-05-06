package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppBannerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppBanner getAppBannerSample1() {
        return new AppBanner().id(1L).title("title1").description("description1").url("url1").priority(1);
    }

    public static AppBanner getAppBannerSample2() {
        return new AppBanner().id(2L).title("title2").description("description2").url("url2").priority(2);
    }

    public static AppBanner getAppBannerRandomSampleGenerator() {
        return new AppBanner()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet());
    }
}
