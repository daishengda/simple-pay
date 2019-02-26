package com.simple.pay.service.message.producer;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.pay.service.message.PayServiceMessageApplicationTests;

/**
 * 	生产者
 * @author daishengda
 *
 */
@Component
public class RabbitSenderTests extends PayServiceMessageApplicationTests {
	
	@Autowired
	private RabbitSender rabbitSender;
	
	@Test
	public void sendTest()
	{
		rabbitSender.send("simple.pay.exchange", "simple.pay.service.#", "Hello World RabbitMQ!".getBytes());
	}
	
	@Test
	public void bindingFanoutTest()
	{
		rabbitSender.bindingFanoutExchange("test.message.queue", "test.message.fanout.exchange");
	}
	
	@Test
	public void bindingTopicTest()
	{
		rabbitSender.bindingTopicExchange("test.message.queue", "test.message.topic.exchange", "test.message.*");
	}
	
	/**
	 * 发送消息到MQ中间件测试
	 */
	@Test
	public void sendMessage() {
		String queueKey = "alipay";
		//使用配置默认的exchange和路由键
		String exchangeKey = "simple.pay.message.exchange";
		String routingKey = "simple.pay.message.*";
		//如果没配置，取队列名作为路由键
		if(StringUtils.isEmpty(routingKey))
		{
			routingKey = queueKey;
		}
		//声明队列以及绑定
		rabbitSender.bindingTopicExchange(queueKey, exchangeKey, routingKey);
		
		// 发送消息到MQ中间件(发送的路由键=配置的路由键+队列名)
		rabbitSender.send(exchangeKey, routingKey.replace("*", queueKey), "Hello World I am simple pay".getBytes());
	}
}
