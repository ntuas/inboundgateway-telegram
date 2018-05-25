package com.nt.ntuas.inboundgateway.telegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Configuration
public class AmqpConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpConfiguration.class);

    private final ConnectionFactory connectionFactory;

    private final List<Queue> queues;

    @Autowired
    public AmqpConfiguration(ConnectionFactory connectionFactory, Optional<List<Queue>> queues) {
        this.connectionFactory = connectionFactory;
        this.queues = queues.orElse(emptyList());
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(String.format("AmpqConfiguration{host='%s', port='%d', vhost='%s', username='%s'}",
                connectionFactory.getHost(), connectionFactory.getPort(), connectionFactory.getVirtualHost(), connectionFactory.getUsername()));
        queues.forEach(rabbitAdmin()::declareQueue);
    }

    @Bean
    @ConditionalOnMissingBean(RabbitAdmin.class)
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

}
