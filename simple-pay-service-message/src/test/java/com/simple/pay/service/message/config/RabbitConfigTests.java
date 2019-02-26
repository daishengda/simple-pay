package com.simple.pay.service.message.config;

import java.util.Date;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.pay.service.message.PayServiceMessageApplicationTests;

/**
 * rabbitmq测试类
 * @author daishengda
 *
 */
@Component
public class RabbitConfigTests extends PayServiceMessageApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Test
	public void sendMsg()
	{
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend("myQueue", "now " + new Date());
	}
}
