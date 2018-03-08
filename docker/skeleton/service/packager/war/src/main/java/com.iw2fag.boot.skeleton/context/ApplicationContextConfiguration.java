package com.iw2fag.boot.skeleton.context;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;


//TODO: use regex to be more generic so we do not need to add every new model explicitly
@Configuration
@ComponentScan(lazyInit = true, basePackages = {"com.iw2fag.lab"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = org.springframework.stereotype.Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = org.springframework.web.bind.annotation.RestController.class)
})
@PropertySources(value = {
        @PropertySource("classpath:skeleton-db.properties"),
})
@EnableAspectJAutoProxy
@EnableCaching
@EnableAsync
public class ApplicationContextConfiguration {


}
