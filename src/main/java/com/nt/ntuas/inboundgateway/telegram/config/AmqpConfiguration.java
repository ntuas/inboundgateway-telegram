package com.nt.ntuas.inboundgateway.telegram.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Configuration
public class AmqpConfiguration {

    private final AmqpAdmin amqpAdmin;

    private final List<Queue> queues;

    @Autowired
    public AmqpConfiguration(AmqpAdmin amqpAdmin, Optional<List<Queue>> queues) {
        this.amqpAdmin = amqpAdmin;
        this.queues = queues.orElse(emptyList());
    }

    @PostConstruct
    protected void init() {
        queues.forEach(amqpAdmin::declareQueue);
    }


}
