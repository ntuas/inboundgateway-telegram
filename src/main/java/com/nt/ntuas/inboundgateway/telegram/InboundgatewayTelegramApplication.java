package com.nt.ntuas.inboundgateway.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.starter.EnableTelegramBots;

@SpringBootApplication
@EnableTelegramBots
public class InboundgatewayTelegramApplication {

	public static void main(String[] args) {
        ApiContextInitializer.init();
		SpringApplication.run(InboundgatewayTelegramApplication.class, args);
	}
}
