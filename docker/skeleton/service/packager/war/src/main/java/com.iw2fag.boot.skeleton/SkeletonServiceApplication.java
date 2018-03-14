package com.iw2fag.boot.skeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.iw2fag.boot.skeleton"})
@EnableAutoConfiguration
public class SkeletonServiceApplication extends SpringBootServletInitializer {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("taskScheduler");
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        getListeners().forEach(servletContext::addListener);

        servletContext.getSessionCookieConfig().setHttpOnly(true);
        servletContext.getSessionCookieConfig().setSecure(useSSL());

        super.onStartup(servletContext);
    }

    public static void main(String[] args) {
        SpringApplication.run(SkeletonServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SkeletonServiceApplication.class);
    }

    private List<EventListener> getListeners() {
        List<EventListener> list = new ArrayList<>();
        list.add(new RequestContextListener());
        return list;
    }

    private boolean useSSL() {
        return false;
    }


}