package com.nt.ntuas.inboundgateway.telegram.bot;

import com.nt.ntuas.inboundgateway.telegram.config.TelegramBotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
public class InboundGatewayTelegramBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboundGatewayTelegramBot.class);

    private TelegramBotProperties telegramBotProperties;

    @Autowired
    public InboundGatewayTelegramBot(TelegramBotProperties telegramBotProperties) {
        super(botOptionsOf(telegramBotProperties));
        this.telegramBotProperties = telegramBotProperties;
    }

    private static DefaultBotOptions botOptionsOf(TelegramBotProperties telegramBotProperties) {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        defaultBotOptions.setBaseUrl(telegramBotProperties.getApiBaseUrl());
        return defaultBotOptions;
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramBotProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                handleIncomingMessage(message);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        LOGGER.info("Received message from chat " + message.getChatId() + " and user " + message.getFrom().getUserName() + ": " + message.getText());
        sendMessage(message.getChatId(), message.getMessageId(), "You said: " + message.getText(), null);
    }

    private void sendMessage(Long chatId, Integer messageId, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }

        sendMessage.setText(text);
        sendApiMethod(sendMessage);
    }
}
