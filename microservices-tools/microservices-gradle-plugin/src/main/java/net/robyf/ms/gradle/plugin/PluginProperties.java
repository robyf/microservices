package net.robyf.ms.gradle.plugin;

import java.io.IOException;
import java.util.Properties;

public class PluginProperties {

    public static final String PLUGIN_VERSION = "version";
    public static final String SPRING_BOOT_VERSION = "springBootVersion";

    private final Properties properties;

    public PluginProperties() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("plugin.properties"));
        } catch (IOException ioe) {
            throw new PluginException("Error reading plugin properties", ioe);
        }
    }

    public String getProperty(final String key) {
        return this.properties.getProperty(key);
    }

}
