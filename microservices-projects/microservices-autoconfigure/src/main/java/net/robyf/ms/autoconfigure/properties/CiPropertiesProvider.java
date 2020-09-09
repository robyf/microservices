package net.robyf.ms.autoconfigure.properties;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

class CiPropertiesProvider implements PropertiesProvider {

    @Override
    public Map<String, Object> getProperties(ConfigurableEnvironment env) {
        String appName = env.getProperty("spring.application.name");
        String appVersion = env.getProperty("spring.application.version");

        Map<String, Object> props = new HashMap<>();

        props.put(PropertyNames.LOGGING_LEVEL_NET_ROBYF_MS, "INFO");

        props.put(PropertyNames.FEIGN_HYSTRIX_ENABLED, true);

        props.put(PropertyNames.INFO_APP_NAME, appName);
        props.put(PropertyNames.INFO_APP_VERSION, appVersion);

        return props;
    }

}
