package com.nt.ntuas.inboundgateway.telegram.test;

import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class EmbeddedAmqpBroker {

    /*
    @Bean(destroyMethod = "shutdown")
    public Broker embeddedAmqpBroker(BrokerOptions embeddedAmqpBrokerOptions) throws Exception {
        Broker broker = new Broker();
        broker.startup(embeddedAmqpBrokerOptions);
        return broker;
    }
*/
}
