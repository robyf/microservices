package net.robyf.ms.autoconfigure;

import net.robyf.ms.autoconfigure.feign.CustomFeignErrorDecoder;
import net.robyf.ms.autoconfigure.jackson.JacksonConfiguration;
import net.robyf.ms.autoconfigure.jackson.JacksonConfigurationLocal;
import net.robyf.ms.autoconfigure.security.FeignConfiguration;
import net.robyf.ms.autoconfigure.security.SecurityConfiguration;
import net.robyf.ms.autoconfigure.security.SecurityFilter;
import net.robyf.ms.autoconfigure.swagger.SwaggerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({
        MicroserviceConfiguration.class,
        FeignConfiguration.class,
        JacksonConfiguration.class,
        JacksonConfigurationLocal.class,
        SecurityConfiguration.class,
        SecurityFilter.class,
        ExceptionHandling.class,
        SwaggerConfiguration.class,
        CustomFeignErrorDecoder.class
})
public @interface EnableMicroserviceAutoconfiguration {
}
