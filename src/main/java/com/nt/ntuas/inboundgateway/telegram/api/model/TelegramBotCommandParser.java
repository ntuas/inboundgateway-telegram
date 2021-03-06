package com.nt.ntuas.inboundgateway.telegram.api.model;

import org.springframework.stereotype.Component;

@Component
public class TelegramBotCommandParser {

    public Command parse(String text) {
        String normalized = text;
        if(text.startsWith("/")) {
            normalized = text.substring(1, text.length());
        }
        int actionEndIdx = normalized.contains(" ") ? normalized.indexOf(" ") : normalized.length();
        String action = normalized.substring(0, actionEndIdx).trim();
        String argument = normalized.substring(actionEndIdx).trim();
        return new Command(action, argument);
    }
}
