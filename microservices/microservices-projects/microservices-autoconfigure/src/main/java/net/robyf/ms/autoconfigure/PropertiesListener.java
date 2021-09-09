package net.robyf.ms.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import net.robyf.ms.autoconfigure.properties.PropertiesConfiguration;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class PropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log.info("Performing microservice autoconfiguration");

        PropertiesConfiguration config = new PropertiesConfiguration(event.getEnvironment());
        config.configure();
    }

}
