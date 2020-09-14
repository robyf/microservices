package net.robyf.ms.autoconfigure.properties;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

public interface PropertiesProvider {

    default Map<String, Object> getProperties(ConfigurableEnvironment env) {
        String appName = env.getProperty("spring.application.name");
        String appVersion = env.getProperty("spring.application.version");

        Map<String, Object> props = new HashMap<>();

        props.put(PropertyNames.LOGGING_LEVEL_NET_ROBYF_MS, "INFO");

        props.put(PropertyNames.FEIGN_HYSTRIX_ENABLED, true);
        props.put(PropertyNames.FEIGN_CLIENT_CONFIG_DEFAULT_CONNECT_TIMEOUT, 10000);
        props.put(PropertyNames.FEIGN_CLIENT_CONFIG_DEFAULT_READ_TIMEOUT, 10000);

        props.put(PropertyNames.HYSTRIX_COMMAND_DEFAULT_EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, 10000);
        props.put(PropertyNames.HYSTRIX_DASHBOARD_PROXY_STREAM_ALLOW_LIST, "*");

        props.put(PropertyNames.INFO_APP_NAME, appName);
        props.put(PropertyNames.INFO_APP_VERSION, appVersion);

        props.put(PropertyNames.SERVER_PORT, 8080);

        props.put(PropertyNames.MANAGEMENT_SERVER_PORT, 8081);
        props.put(PropertyNames.MANAGEMENT_SERVER_SERVLET_CONTEXT_PATH, "/");
        props.put(PropertyNames.MANAGEMENT_ENDPOINTS_ENABLED_BY_DEFAULT, false);
        props.put(PropertyNames.MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE, "hystrix.stream, health, info");
        props.put(PropertyNames.MANAGEMENT_ENDPOINT_INFO_ENABLED, true);
        props.put(PropertyNames.MANAGEMENT_ENDPOINT_HEALTH_ENABLED, true);
        props.put(PropertyNames.MANAGEMENT_ENDPOINT_HYSTRIX_STREAM_ENABLED, true);
        props.put(PropertyNames.MANAGEMENT_ENDPOINT_SHUTDOWN_ENABLED, false);

        props.putAll(getProfileSpecificProperties());

        return props;
    }

    Map<String, Object> getProfileSpecificProperties();

}
