package com.nt.ntuas.inboundgateway.telegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(ProductBackendProperties.class)
public class ProductBackendConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBackendConfiguration.class);

    private final ProductBackendProperties productBackendProperties;

    @Autowired
    public ProductBackendConfiguration(ProductBackendProperties productBackendProperties) {
        this.productBackendProperties = productBackendProperties;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(productBackendProperties.toString());
    }

    @Bean
    public Queue productQueue() {
        return new Queue(productBackendProperties.getQueue(), true);
    }

}
