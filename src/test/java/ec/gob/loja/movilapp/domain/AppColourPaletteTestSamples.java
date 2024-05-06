package ec.gob.loja.movilapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppColourPaletteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppColourPalette getAppColourPaletteSample1() {
        return new AppColourPalette()
            .id(1L)
            .description("description1")
            .primaryColour("primaryColour1")
            .secondaryColour("secondaryColour1")
            .tertiaryColour("tertiaryColour1")
            .neutralColour("neutralColour1")
            .ligthBackgroundColour("ligthBackgroundColour1")
            .darkBackgroundColour("darkBackgroundColour1");
    }

    public static AppColourPalette getAppColourPaletteSample2() {
        return new AppColourPalette()
            .id(2L)
            .description("description2")
            .primaryColour("primaryColour2")
            .secondaryColour("secondaryColour2")
            .tertiaryColour("tertiaryColour2")
            .neutralColour("neutralColour2")
            .ligthBackgroundColour("ligthBackgroundColour2")
            .darkBackgroundColour("darkBackgroundColour2");
    }

    public static AppColourPalette getAppColourPaletteRandomSampleGenerator() {
        return new AppColourPalette()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .primaryColour(UUID.randomUUID().toString())
            .secondaryColour(UUID.randomUUID().toString())
            .tertiaryColour(UUID.randomUUID().toString())
            .neutralColour(UUID.randomUUID().toString())
            .ligthBackgroundColour(UUID.randomUUID().toString())
            .darkBackgroundColour(UUID.randomUUID().toString());
    }
}
