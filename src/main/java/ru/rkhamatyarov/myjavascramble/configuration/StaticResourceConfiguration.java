package ru.rkhamatyarov.myjavascramble.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for serving static image resources.
 */
@Configuration
public final class StaticResourceConfiguration implements WebMvcConfigurer {

    /**
     * Add resource handlers for serving images.
     *
     * @param registry the registry to add resource handlers to
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/");
    }
}
