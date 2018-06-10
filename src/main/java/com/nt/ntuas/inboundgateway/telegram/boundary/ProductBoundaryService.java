package com.nt.ntuas.inboundgateway.telegram.boundary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Component
public class ProductBoundaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBoundaryService.class);

    private final AmqpTemplate amqpTemplate;
    private final AsyncAmqpTemplate asyncAmqpTemplate;

    private final Queue productQueue;

    @Autowired
    public ProductBoundaryService(AmqpTemplate amqpTemplate, AsyncAmqpTemplate asyncAmqpTemplate, Queue productQueue) {
        this.amqpTemplate = amqpTemplate;
        this.asyncAmqpTemplate = asyncAmqpTemplate;
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

    public CompletableFuture<Map<String, Integer>> countAsync(Set<String> products) {
        if(products.isEmpty()) {
            LOGGER.info("Query amount of available pieces of all products");
            return sendAndReceiveAsync("count", "").thenApply(this::asIntegerValueMap);
        }
        LOGGER.info("Query amount of available pieces of products: " + products);
        Set<CompletableFuture<Map<String, Integer>>> results = products.stream()
                .map(product -> sendAndReceiveAsync("count", product))
                .map(f -> f.thenApply(this::asIntegerValueMap))
                .collect(toSet());
        return CompletableFuture.supplyAsync(() -> results.stream()
                .<Map<String, Integer>>map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.warn(e.getMessage(), e);
                        return new HashMap<>();
                    }
                })
                .flatMap(m -> m.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private void sendRequest(String action, String body) {
        amqpTemplate.send(productQueue.getName(), createMessage(action, body));
    }

    private CompletableFuture<Map<String, Object>> sendAndReceiveAsync(String action, String body) {
        ListenableFuture<Message> resultFuture = asyncAmqpTemplate.sendAndReceive(productQueue.getName(), createMessage(action, body));
        return resultFuture.completable().thenApplyAsync(this::messageToMap);
    }

    private Message createMessage(String action, String body) {
        return MessageBuilder.withBody(body.getBytes(UTF_8))
                .andProperties(MessagePropertiesBuilder.newInstance()
                        .setCorrelationId(randomUUID().toString())
                        .setHeader("action", action).build())
                .build();
    }

    private Map<String, Object> messageToMap(Message message) {
        try {
            return new ObjectMapper().readValue(new String(message.getBody(), UTF_8), new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            LOGGER.warn(format("Could not deserialize message of response with correlation id %s.", message.getMessageProperties().getCorrelationId()), e);
            return new HashMap<>();
        }
    }

    private Map<String, Integer> asIntegerValueMap(Map<String, Object> source) {
        return source.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), Integer.valueOf(entry.getValue().toString())))
                .collect(toMap(Pair::getKey, Pair::getValue));
    }
}
