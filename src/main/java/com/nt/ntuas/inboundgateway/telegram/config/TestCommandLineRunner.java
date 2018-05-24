package com.nt.ntuas.inboundgateway.telegram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.LongPollingBot;
import org.telegram.telegrambots.generics.WebhookBot;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Configuration
public class TestCommandLineRunner implements CommandLineRunner {

    private final List<LongPollingBot> longPollingBots;
    private final List<WebhookBot> webHookBots;

    @Autowired
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    public TestCommandLineRunner(Optional<List<LongPollingBot>> longPollingBots,
                                 Optional<List<WebhookBot>> webHookBots) {
        this.longPollingBots = longPollingBots.orElse(emptyList());
        this.webHookBots = webHookBots.orElse(emptyList());
    }

    @Override
    public void run(String... args) {
        try {
            for (LongPollingBot bot : longPollingBots) {
                telegramBotsApi.registerBot(bot);
            }
            for (WebhookBot bot : webHookBots) {
                telegramBotsApi.registerBot(bot);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Bean
    @ConditionalOnMissingBean(TelegramBotsApi.class)
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi();
    }
}
