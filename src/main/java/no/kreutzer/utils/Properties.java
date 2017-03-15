package no.kreutzer.utils;

import restx.config.ConfigLoader;
import restx.config.ConfigSupplier;
import restx.factory.Module;
import restx.factory.Provides;

@Module
public class Properties {
    @Provides
    public ConfigSupplier appConfigSupplier(ConfigLoader configLoader) {
        // Load settings.properties in no.kreutzer package as a set of config entries
        return configLoader.fromResource("no/kreutzer/settings");
    }
	
}
