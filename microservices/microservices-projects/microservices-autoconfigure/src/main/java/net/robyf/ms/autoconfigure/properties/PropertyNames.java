package net.robyf.ms.autoconfigure.properties;

abstract class PropertyNames {

    static final String LOGGING_LEVEL_NET_ROBYF_MS = "logging.level.net.robyf.ms";

    static final String FEIGN_HYSTRIX_ENABLED = "feign.hystrix.enabled";
    static final String FEIGN_CLIENT_CONFIG_DEFAULT_CONNECT_TIMEOUT = "feign.client.config.default.connectTimeout";
    static final String FEIGN_CLIENT_CONFIG_DEFAULT_READ_TIMEOUT = "feign.client.config.default.readTimeout";

    static final String HYSTRIX_COMMAND_DEFAULT_EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS = "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds";
    static final String HYSTRIX_DASHBOARD_PROXY_STREAM_ALLOW_LIST = "hystrix.dashboard.proxy-stream-allow-list";

    static final String INFO_APP_NAME = "info.app.name";
    static final String INFO_APP_VERSION = "info.app.version";

    static final String SERVER_PORT = "server.port";

    static final String MANAGEMENT_SERVER_PORT = "management.server.port";
    static final String MANAGEMENT_SERVER_SERVLET_CONTEXT_PATH = "management.server.servlet.context-path";
    static final String MANAGEMENT_ENDPOINTS_ENABLED_BY_DEFAULT = "management.endpoints.enabled-by-default";
    static final String MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE = "management.endpoints.web.exposure.include";
    static final String MANAGEMENT_ENDPOINT_INFO_ENABLED = "management.endpoint.info.enabled";
    static final String MANAGEMENT_ENDPOINT_HEALTH_ENABLED = "management.endpoint.health.enabled";
    static final String MANAGEMENT_ENDPOINT_HYSTRIX_STREAM_ENABLED = "management.endpoint.hystrix.stream.enabled";
    static final String MANAGEMENT_ENDPOINT_SHUTDOWN_ENABLED = "management.endpoint.shutdown.enabled";

    private PropertyNames() {
    }

}
