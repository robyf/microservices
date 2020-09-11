package net.robyf.ms.autoconfigure.properties;

import java.util.HashMap;
import java.util.Map;

class LocalPropertiesProvider implements PropertiesProvider {

    @Override
    public Map<String, Object> getProfileSpecificProperties() {
        Map<String, Object> props = new HashMap<>();

        props.put(PropertyNames.LOGGING_LEVEL_NET_ROBYF_MS, "DEBUG");

        return props;
    }

}
