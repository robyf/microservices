package net.robyf.ms.autoconfigure.properties;

abstract class PropertyNames {

    static final String LOGGING_LEVEL_NET_ROBYF_MS = "logging.level.net.robyf.ms";

    static final String FEIGN_HYSTRIX_ENABLED = "feign.hystrix.enabled";
    static final String FEIGN_CLIENT_CONFIG_DEFAULT_CONNECT_TIMEOUT = "feign.client.config.default.connectTimeout";
    static final String FEIGN_CLIENT_CONFIG_DEFAULT_READ_TIMEOUT = "feign.client.config.default.readTimeout";

    static final String HYSTRIX_COMMAND_DEFAULT_EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS = "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds";

    static final String INFO_APP_NAME = "info.app.name";
    static final String INFO_APP_VERSION = "info.app.version";

    private PropertyNames() {
    }

}
