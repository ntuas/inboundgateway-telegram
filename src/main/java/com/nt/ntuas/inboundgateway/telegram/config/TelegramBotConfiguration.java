package com.nt.ntuas.inboundgateway.telegram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.LongPollingBot;
import org.telegram.telegrambots.generics.WebhookBot;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    private final TelegramBotProperties telegramBotProperties;

    private final List<LongPollingBot> longPollingBots;
    private final List<WebhookBot> webHookBots;

    @Autowired
    public TelegramBotConfiguration(TelegramBotProperties telegramBotProperties,
                                    Optional<List<LongPollingBot>> longPollingBots,
                                    Optional<List<WebhookBot>> webHookBots) {
        this.telegramBotProperties = telegramBotProperties;
        this.longPollingBots = longPollingBots.orElse(emptyList());
        this.webHookBots = webHookBots.orElse(emptyList());
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(telegramBotProperties.toString());
        ApiContextInitializer.init();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiRequestException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        for (LongPollingBot bot : longPollingBots) {
            telegramBotsApi.registerBot(bot);
        }
        for (WebhookBot bot : webHookBots) {
            telegramBotsApi.registerBot(bot);
        }
        return telegramBotsApi;
    }
}
