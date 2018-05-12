package com.nt.ntuas.inboundgateway.telegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    private TelegramBotProperties telegramBotProperties;

    @Autowired
    public TelegramBotConfiguration(TelegramBotProperties telegramBotProperties) {
        this.telegramBotProperties = telegramBotProperties;
    }

    @PostConstruct
    protected void logProperties() {
        LOGGER.info(telegramBotProperties.toString());
    }
}
