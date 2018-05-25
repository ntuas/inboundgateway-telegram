package com.nt.ntuas.inboundgateway.telegram.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductBoundaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBoundaryService.class);

    private final AmqpTemplate amqpTemplate;

    private final Queue productQueue;

    @Autowired
    public ProductBoundaryService(AmqpTemplate amqpTemplate, Queue productQueue) {
        this.amqpTemplate = amqpTemplate;
        this.productQueue = productQueue;
    }

    public void putProduct(String product) {
        LOGGER.info("Have to put product " + product);
        sendRequest("put", product);
    }

    public void takeProduct(String product) {
        LOGGER.info("Have to take product " + product);
        sendRequest("take", product);
    }

    public void orderProducts() {
        LOGGER.info("Should order products");
        sendRequest("order", "");
    }

    private void sendRequest(String action, String body) {
        amqpTemplate.send(productQueue.getName(), createMessage(action, body));
    }

    private Message createMessage(String action, String body) {
        return MessageBuilder.withBody(body.getBytes())
                .andProperties(MessagePropertiesBuilder.newInstance()
                        .setHeader("action", action).build())
                .build();
    }
}
