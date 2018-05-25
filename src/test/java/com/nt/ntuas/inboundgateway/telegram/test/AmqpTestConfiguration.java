package com.nt.ntuas.inboundgateway.telegram.test;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class AmqpTestConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory() {
            @Override
            public Connection createConnection() throws AmqpException {
                return null;
            }

            @Override
            public String getHost() {
                return "localhost";
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public String getVirtualHost() {
                return "default";
            }

            @Override
            public String getUsername() {
                return "root";
            }

            @Override
            public void addConnectionListener(ConnectionListener listener) {

            }

            @Override
            public boolean removeConnectionListener(ConnectionListener listener) {
                return false;
            }

            @Override
            public void clearConnectionListeners() {

            }
        };
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return mock(RabbitAdmin.class);
    }
}
