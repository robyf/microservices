package net.robyf.ms.autoconfigure.properties;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

public interface PropertiesProvider {

    Map<String, Object> getProperties(ConfigurableEnvironment env);

}
