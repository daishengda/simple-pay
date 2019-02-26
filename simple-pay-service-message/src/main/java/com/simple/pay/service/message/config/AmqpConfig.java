package com.simple.pay.service.message.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {
	
	@Value("${message.exchange}")
	private String messageExchange; 

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		initDeclareExchange(rabbitAdmin);
		return rabbitAdmin;
	}
	
	/**
	 * 初始化声明的exchange
	 * @param rabbitAdmin
	 */
	private void initDeclareExchange(RabbitAdmin rabbitAdmin)
	{
		rabbitAdmin.declareExchange(new TopicExchange(messageExchange, true, false));
	}
}
