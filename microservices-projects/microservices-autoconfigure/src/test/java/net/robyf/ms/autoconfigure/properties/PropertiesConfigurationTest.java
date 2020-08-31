package net.robyf.ms.autoconfigure.properties;

import net.robyf.ms.autoconfigure.AutoconfigurationException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesConfigurationTest {

    private final ConfigurableEnvironment context = Mockito.mock(ConfigurableEnvironment.class);
    private final PropertiesConfiguration config = new PropertiesConfiguration(context);

    @Test
    public void testLocalProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"local"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.LOCAL);
    }

    @Test
    public void testLocalVariantProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"local-foo"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.LOCAL);
    }

    @Test
    public void testTestProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"test"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.LOCAL);
    }

    @Test
    public void testTestVariantProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"test-foo"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.LOCAL);
    }

    @Test
    public void testDevProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"dev"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.DEV);
    }

    @Test
    public void testDevVariantProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"dev-foo"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.DEV);
    }

    @Test
    public void testCiProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"ci"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.CI);
    }

    @Test
    public void testCiVariantProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"ci-foo"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.CI);
    }

    @Test
    public void testProdProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"prod"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.PROD);
    }

    @Test
    public void testProdVariantProfile() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"prod-foo"});

        assertThat(config.resolveProfile()).isEqualTo(Profile.PROD);
    }

    @Test(expected = AutoconfigurationException.class)
    public void testNoValidProfilePresent() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{});

        config.resolveProfile();
    }

    @Test(expected = AutoconfigurationException.class)
    public void testMultipleProfilesPresent() {
        Mockito.when(context.getActiveProfiles()).thenReturn(new String[]{"local", "dev"});

        config.resolveProfile();
    }

}
