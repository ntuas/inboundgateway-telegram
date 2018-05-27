package com.nt.ntuas.inboundgateway.telegram.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;

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

    public String countProduct(String product) {
        String response = sendAndReceive("count", product);
        LOGGER.info("Queried amount of available pieces of product " + product + ": " + response);
        return response;
    }

    private void sendRequest(String action, String body) {
        amqpTemplate.send(productQueue.getName(), createMessage(action, body));
    }

    private String sendAndReceive(String action, String body) {
        Message response = amqpTemplate.sendAndReceive(productQueue.getName(), createMessage(action, body));
        return new String(response.getBody(), UTF_8);
    }

    private Message createMessage(String action, String body) {
        return MessageBuilder.withBody(body.getBytes())
                .andProperties(MessagePropertiesBuilder.newInstance()
                        .setCorrelationId(randomUUID().toString())
                        .setHeader("action", action).build())
                .build();
    }
}
