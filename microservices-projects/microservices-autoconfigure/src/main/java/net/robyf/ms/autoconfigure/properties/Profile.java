package net.robyf.ms.autoconfigure.properties;

public enum Profile {

    LOCAL(new LocalPropertiesProvider()),
    DEV(new DevPropertiesProvider()),
    CI(new CiPropertiesProvider()),
    PROD(new ProdPropertiesProvider());

    final PropertiesProvider propertiesProvider;

    Profile(final PropertiesProvider provider) {
        this.propertiesProvider = provider;
    }

    public PropertiesProvider getPropertiesProvider() {
        return this.propertiesProvider;
    }

}
