package net.robyf.ms.autoconfigure.properties;

import java.util.Collections;
import java.util.Map;

class ProdPropertiesProvider implements PropertiesProvider {

    @Override
    public Map<String, Object> getProfileSpecificProperties() {
        return Collections.emptyMap();
    }

}
