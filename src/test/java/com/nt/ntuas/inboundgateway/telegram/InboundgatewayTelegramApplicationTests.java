package com.nt.ntuas.inboundgateway.telegram;

import com.nt.ntuas.inboundgateway.telegram.test.AmqpTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(AmqpTestConfiguration.class)
public class InboundgatewayTelegramApplicationTests {

	@Test
	public void contextLoads() {
	}

}
