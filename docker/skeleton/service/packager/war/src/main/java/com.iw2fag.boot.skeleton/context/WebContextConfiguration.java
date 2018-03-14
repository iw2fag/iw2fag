package com.iw2fag.boot.skeleton.context;

import com.google.common.cache.CacheBuilderSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.iw2fag.lab.controller"}, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = org.springframework.stereotype.Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = org.springframework.web.bind.annotation.RestController.class)
})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
public class WebContextConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment environment;

 /*   @Bean //temp solution since cache was not recognize
    public GuavaCacheManager cacheManager(@Value("${spring.cache.cache-names}") String cacheNames, @Value("${spring.cache.guava.spec}") String cacheBuilderSpecification) {
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager(cacheNames.split(","));
        guavaCacheManager.setCacheBuilderSpec(CacheBuilderSpec.parse(cacheBuilderSpecification));
        return guavaCacheManager;
    }*/


    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        String property = environment.getProperty("server.upload.maxUploadSize", "209715200");
        long maxUploadSize = Long.parseLong(property);
        multipartResolver.setMaxUploadSize(maxUploadSize);
        return multipartResolver;
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
