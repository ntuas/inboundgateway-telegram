package com.nt.ntuas.inboundgateway.telegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.ApiConstants;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@ConfigurationProperties("inboundgateway.telegram.bot")
@Validated
public class TelegramBotProperties {

    @NotBlank
    private String apiBaseUrl = ApiConstants.BASE_URL;
    @NotBlank
    private String username;
    @NotBlank
    private String token;

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramBotProperties that = (TelegramBotProperties) o;
        return Objects.equals(apiBaseUrl, that.apiBaseUrl) &&
                Objects.equals(username, that.username) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiBaseUrl, username, token);
    }

    @Override
    public String toString() {
        return "TelegramBotProperties{" +
                "apiBaseUrl='" + apiBaseUrl + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
