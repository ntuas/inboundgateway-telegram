package com.nt.ntuas.inboundgateway.telegram.api.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TelegramBotCommandParserTest {

    TelegramBotCommandParser commandParser = new TelegramBotCommandParser();

    @Test
    public void shouldParseCommandWithMarkedAction() {
        Command command = commandParser.parse("/test");

        assertThat(command.getAction(), is("test"));
        assertThat(command.getArgument(), is(""));
    }

    @Test
    public void shouldParseCommandWithUnmarkedAction() {
        Command command = commandParser.parse("test");

        assertThat(command.getAction(), is("test"));
        assertThat(command.getArgument(), is(""));
    }

    @Test
    public void shouldParseCommandWithMarkedActionAndArgument() {
        Command command = commandParser.parse("/test some argument");

        assertThat(command.getAction(), is("test"));
        assertThat(command.getArgument(), is("some argument"));
    }
}