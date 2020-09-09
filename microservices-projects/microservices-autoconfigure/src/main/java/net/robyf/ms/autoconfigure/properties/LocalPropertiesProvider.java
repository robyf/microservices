package net.robyf.ms.autoconfigure.properties;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

class LocalPropertiesProvider implements PropertiesProvider {

    @Override
    public Map<String, Object> getProperties(ConfigurableEnvironment env) {
        String appName = env.getProperty("spring.application.name");
        String appVersion = env.getProperty("spring.application.version");

        Map<String, Object> props = new HashMap<>();

        props.put(PropertyNames.LOGGING_LEVEL_NET_ROBYF_MS, "DEBUG");

        props.put(PropertyNames.FEIGN_HYSTRIX_ENABLED, true);
        props.put(PropertyNames.FEIGN_CLIENT_CONFIG_DEFAULT_CONNECT_TIMEOUT, 10000);
        props.put(PropertyNames.FEIGN_CLIENT_CONFIG_DEFAULT_READ_TIMEOUT, 10000);

        props.put(PropertyNames.HYSTRIX_COMMAND_DEFAULT_EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, 10000);

        props.put(PropertyNames.INFO_APP_NAME, appName);
        props.put(PropertyNames.INFO_APP_VERSION, appVersion);

        return props;
    }

}
