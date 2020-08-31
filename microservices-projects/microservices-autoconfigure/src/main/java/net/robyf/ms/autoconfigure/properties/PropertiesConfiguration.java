package net.robyf.ms.autoconfigure.properties;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.AutoconfigurationException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

@Slf4j
public class PropertiesConfiguration {

    private final ConfigurableEnvironment context;

    public PropertiesConfiguration(final ConfigurableEnvironment context) {
        this.context = context;
    }

    Profile resolveProfile() {
        boolean isLocal = false;
        boolean isDev = false;
        boolean isCi = false;
        boolean isProd = false;

        for (String profile : context.getActiveProfiles()) {
            if ("local".equals(profile) || profile.startsWith("local-") || "test".equals(profile) || profile.startsWith("test-")) {
                isLocal = true;
            }
            if ("dev".equals(profile) || profile.startsWith("dev-")) {
                isDev = true;
            }
            if ("ci".equals(profile) || profile.startsWith("ci-")) {
                isCi = true;
            }
            if ("prod".equals(profile) || profile.startsWith("prod-")) {
                isProd = true;
            }
        }

        int count = (isLocal ? 1 : 0) + (isDev ? 1 : 0) + (isCi ? 1 : 0) + (isProd ? 1 : 0);
        if (count == 0) {
            throw new AutoconfigurationException("No valid profile found. One between local, local-*, test, test-*, dev, dev-*, ci, ci-*, prod, prod-* must be active");
        }
        if (count > 1) {
            throw new AutoconfigurationException("Multiple profiles found. One and only one between local, local-*, test, test-*, dev, dev-*, ci, ci-*, prod, prod-* must be active");
        }

        if (isLocal) {
            return Profile.LOCAL;
        }

        if (isDev) {
            return Profile.DEV;
        }

        if (isCi) {
            return Profile.CI;
        }

        return Profile.PROD;
    }

    private void assertPropertyPresent(final String propertyName) {
        if (this.context.getProperty(propertyName) == null) {
            throw new AutoconfigurationException("Property '" + propertyName + "' missing from bootstrap context. Please define it in bootstrap.yml");
        }
    }

    public void configure() {
        Profile profile = resolveProfile();
        log.info("Performing autoconfiguration for profile {}", profile);

        assertPropertyPresent("spring.application.name");
        assertPropertyPresent("spring.application.version");

        this.context.getPropertySources().addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new MapPropertySource("autoconfigured", profile.getPropertiesProvider().getProperties(this.context)));
    }

}
