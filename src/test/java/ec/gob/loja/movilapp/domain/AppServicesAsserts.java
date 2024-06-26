package ec.gob.loja.movilapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AppServicesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppServicesAllPropertiesEquals(AppServices expected, AppServices actual) {
        assertAppServicesAutoGeneratedPropertiesEquals(expected, actual);
        assertAppServicesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppServicesAllUpdatablePropertiesEquals(AppServices expected, AppServices actual) {
        assertAppServicesUpdatableFieldsEquals(expected, actual);
        assertAppServicesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppServicesAutoGeneratedPropertiesEquals(AppServices expected, AppServices actual) {
        assertThat(expected)
            .as("Verify AppServices auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppServicesUpdatableFieldsEquals(AppServices expected, AppServices actual) {
        assertThat(expected)
            .as("Verify AppServices relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getUrl()).as("check url").isEqualTo(actual.getUrl()))
            .satisfies(e -> assertThat(e.getIcon()).as("check icon").isEqualTo(actual.getIcon()))
            .satisfies(e -> assertThat(e.getBackgroundCard()).as("check backgroundCard").isEqualTo(actual.getBackgroundCard()))
            .satisfies(e -> assertThat(e.getIsActive()).as("check isActive").isEqualTo(actual.getIsActive()))
            .satisfies(e -> assertThat(e.getPriority()).as("check priority").isEqualTo(actual.getPriority()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppServicesUpdatableRelationshipsEquals(AppServices expected, AppServices actual) {
        assertThat(expected)
            .as("Verify AppServices relationships")
            .satisfies(e -> assertThat(e.getApplication()).as("check application").isEqualTo(actual.getApplication()));
    }
}
