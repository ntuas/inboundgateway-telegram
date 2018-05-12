package com.nt.ntuas.inboundgateway.telegram.bot;

import com.nt.ntuas.inboundgateway.telegram.config.TelegramBotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;

@Component
public class InboundGatewayTelegramBot extends ConfigurableTelegramBot {

    @Autowired
    public InboundGatewayTelegramBot(TelegramBotProperties telegramBotProperties) {
        super(telegramBotProperties);
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

}
