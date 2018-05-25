package com.nt.ntuas.inboundgateway.telegram.config;

import com.nt.ntuas.inboundgateway.telegram.api.TelegramBotProductServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    private final TelegramBotProperties telegramBotProperties;

    @Autowired
    public TelegramBotConfiguration(TelegramBotProperties telegramBotProperties) {
        this.telegramBotProperties = telegramBotProperties;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(telegramBotProperties.toString());
    }

    @Bean
    public BotSession inboundGatewayTelegramBotSession(TelegramBotProductServiceController bot) {
        BotSession session = new DefaultBotSession();
        session.setToken(bot.getBotToken());
        session.setOptions(bot.getOptions());
        session.setCallback(bot);
        session.start();
        return session;
    }

}
