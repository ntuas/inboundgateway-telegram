package com.nt.ntuas.inboundgateway.telegram.api;

import com.nt.ntuas.inboundgateway.telegram.api.model.Command;
import com.nt.ntuas.inboundgateway.telegram.api.model.TelegramBotCommandParser;
import com.nt.ntuas.inboundgateway.telegram.boundary.ProductBoundaryService;
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

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Component
public class TelegramBotProductServiceController extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotProductServiceController.class);

    private final TelegramBotProperties telegramBotProperties;

    private final ProductBoundaryService productBoundaryService;

    private final TelegramBotCommandParser commandParser;

    @Autowired
    public TelegramBotProductServiceController(TelegramBotProperties telegramBotProperties,
                                               ProductBoundaryService productBoundaryService,
                                               TelegramBotCommandParser commandParser) {
        super(botOptionsOf(telegramBotProperties));
        this.telegramBotProperties = telegramBotProperties;
        this.productBoundaryService = productBoundaryService;
        this.commandParser = commandParser;
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
        Command command = commandParser.parse(message.getText());
        if ("put".equalsIgnoreCase(command.getAction())) {
            productBoundaryService.putProduct(command.getArgument());
            confirmMessage(message, format("put product %s", command.getArgument()));
        } else if ("take".equalsIgnoreCase(command.getAction())) {
            productBoundaryService.takeProduct(command.getArgument());
            confirmMessage(message, format("take product %s", command.getArgument()));
        } else if ("order".equalsIgnoreCase(command.getAction())) {
            productBoundaryService.orderProducts();
            confirmMessage(message, "order all products");
        } else if ("count".equalsIgnoreCase(command.getAction())) {
            //int count = productBoundaryService.countProduct(command.getArgument());
            //confirmMessage(message, format("amount of available pieces of product %s is %d", command.getArgument(), count));
            CompletableFuture<Map<String, Integer>> resultFuture = productBoundaryService.countAsync(stream(command.getArgument().split(" ")).collect(toSet()));
            resultFuture.thenAcceptAsync(map -> {
                LOGGER.info(String.format("Received response for chat id %s and user %s: %s", message.getChatId(), message.getFrom().getUserName(), map));
                try {
                    confirmMessage(message, String.valueOf(map));
                } catch (TelegramApiException e) {
                    LOGGER.warn(String.format("Could not send response to chat id %s and user %s", message.getChatId(), message.getFrom().getUserName()), e);
                }
            });
        } else {
            confirmMessage(message, "Use one of the following commands:\n" +
                    "/put <product>\n" +
                    "/take <product>\n" +
                    "/order\n" +
                    "/count <product>");
        }
    }

    private void confirmMessage(Message message, String text) throws TelegramApiException {
        sendMessage(message.getChatId(), message.getMessageId(), text, null);
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
