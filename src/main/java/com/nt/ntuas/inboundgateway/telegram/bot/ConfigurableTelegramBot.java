package com.nt.ntuas.inboundgateway.telegram.bot;

import com.nt.ntuas.inboundgateway.telegram.config.TelegramBotProperties;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public abstract class ConfigurableTelegramBot extends TelegramLongPollingBot {

    private TelegramBotProperties telegramBotProperties;

    public ConfigurableTelegramBot(TelegramBotProperties telegramBotProperties) {
        super(botOptionsOf(telegramBotProperties));
        this.telegramBotProperties = telegramBotProperties;
    }

    private static DefaultBotOptions botOptionsOf(TelegramBotProperties telegramBotProperties) {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setBaseUrl(telegramBotProperties.getApiBaseUrl());
        return botOptions;
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }
}
