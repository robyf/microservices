package net.robyf.ms.autoconfigure.properties;

import java.util.HashMap;
import java.util.Map;

class LocalPropertiesProvider implements PropertiesProvider {

    @Override
    public Map<String, Object> getProfileSpecificProperties() {
        Map<String, Object> props = new HashMap<>();

        props.put(PropertyNames.LOGGING_LEVEL_NET_ROBYF_MS, "DEBUG");

        props.put(PropertyNames.SERVER_PORT, null);

        props.put(PropertyNames.MANAGEMENT_SERVER_PORT, null);
        props.put(PropertyNames.MANAGEMENT_ENDPOINTS_ENABLED_BY_DEFAULT, true);
        props.put(PropertyNames.MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE, "*");

        return props;
    }

}
