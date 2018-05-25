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
        int argumentStartIndex = normalized.contains(" ") ? normalized.indexOf(" ") + 1 : normalized.length();
        String action = normalized.substring(0, actionEndIdx);
        String argument = normalized.substring(argumentStartIndex);
        return new Command(action, argument);
    }
}
