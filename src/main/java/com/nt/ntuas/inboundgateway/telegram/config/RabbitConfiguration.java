package com.nt.ntuas.inboundgateway.telegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfiguration.class);

    private final ConnectionFactory connectionFactory;

    public RabbitConfiguration(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(String.format("RabbitConfiguration{host='%s', port='%d', vhost='%s', username='%s'}",
                connectionFactory.getHost(), connectionFactory.getPort(), connectionFactory.getVirtualHost(), connectionFactory.getUsername()));
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
