package com.iw2fag.boot.skeleton.context;

import com.iw2fag.lab.data.auditing.AuditorAwareImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

//TODO: use regex to be more generic so we do not need to add every new model explicitly
//@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
//@EnableJpaRepositories(basePackages = {"com.iw2fag.lab.repository"})
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class InfrastructureConfiguration implements EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfrastructureConfiguration.class);

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }


   /* @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(environment.getProperty("db.hibernate.connection.username"));
        dataSource.setPassword(environment.getProperty("db.hibernate.connection.password"));
        dataSource.setJdbcUrl(environment.getProperty("db.hibernate.connection.url"));
        dataSource.setDriverClassName(environment.getProperty("db.hibernate.connection.driver_class"));
        String poolSizeStr = environment.getProperty("db.connection.pool.size", "30");
        LOGGER.info("connection pool size {}", poolSizeStr);
        dataSource.setMaximumPoolSize(Integer.parseInt(poolSizeStr));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, String> jpaPropertyMap = new HashMap<String, String>();
        jpaPropertyMap.put("hibernate.hbm2ddl.auto", environment.getProperty("db.hibernate.hbm2ddl.auto"));
        jpaPropertyMap.put("hibernate.hbm2ddl.import_files", environment.getProperty("db.hibernate.hbm2ddl.import_files"));
        jpaPropertyMap.put("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
        jpaPropertyMap.put("hibernate.cache.use_second_level_cache", "true");
        jpaPropertyMap.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        jpaPropertyMap.put("hibernate.cache.use_query_cache", "true");
        jpaPropertyMap.put("hibernate.connection.CharSet", "utf8");
        jpaPropertyMap.put("hibernate.connection.characterEncoding", "utf8");
        jpaPropertyMap.put("hibernate.connection.useUnicode", "true");
        jpaPropertyMap.put("hibernate.show_sql", "false");
        jpaPropertyMap.put("hibernate.format_sql", "false");
        jpaPropertyMap.put("javax.persistence.lock.timeout", "3000");
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        //TODO: use regex to be more generic so we do not need to add every new model explicitly
        entityManagerFactory.setPackagesToScan("com.iw2fag.lab.model");
        entityManagerFactory.setJpaVendorAdapter(jpaAdapter());
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setJpaPropertyMap(jpaPropertyMap);
        return entityManagerFactory;
    }

    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory().getObject().createEntityManager();
    }

    @Bean
    public HibernateJpaVendorAdapter jpaAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabasePlatform(environment.getProperty("db.hibernate.dialect"));
        return jpaVendorAdapter;
    }

    @Bean
    public RequiredAnnotationBeanPostProcessor requiredAnnotationBeanPostProcessor() {
        return new RequiredAnnotationBeanPostProcessor();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "auditorAware")
    public AuditorAware auditorAware() {
        return new AuditorAwareImpl();
    }*/

}
