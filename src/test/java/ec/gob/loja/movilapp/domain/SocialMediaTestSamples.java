package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SocialMediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SocialMedia getSocialMediaSample1() {
        return new SocialMedia().id(1L).title("title1").url("url1").icon("icon1").colour("colour1");
    }

    public static SocialMedia getSocialMediaSample2() {
        return new SocialMedia().id(2L).title("title2").url("url2").icon("icon2").colour("colour2");
    }

    public static SocialMedia getSocialMediaRandomSampleGenerator() {
        return new SocialMedia()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .colour(UUID.randomUUID().toString());
    }
}
