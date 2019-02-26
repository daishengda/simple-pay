package com.simple.pay.service.message.producer;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 生产者
 * 
 * @author daishengda
 *
 */
@Component
public class RabbitSender {

	private static final Log log = LogFactory.getLog(RabbitSender.class);

	// 自动注入RabbitTemplate模板类
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Value("${message.exchange}")
	private String messageExchange;

	// 回调函数: confirm确认
	final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			log.info("correlationData: " + correlationData);
			log.info("ack：" + ack);
			if (!ack) {
				log.error(correlationData + "ack异常！");
			}
		}
	};

	// 回调函数: return返回
	final ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {

		@Override
		public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
				String routingKey) {
			log.error("return exchange: " + exchange + ", routingKey: " + routingKey + ", replyCode: " + replyCode
					+ ", replyText: " + replyText);
		}
	};

	public void send(String exchange, String routingKey, Object message) {
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		// id + 时间戳 全局唯一
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
	}

	public void bindingFanoutExchange(String queueKey, String exchangeKey) {
		FanoutExchange exchange = new FanoutExchange(exchangeKey, true, false);
		Queue queue = new Queue(queueKey, true);
		bindingFanoutExchange(queue, exchange);
	}

	public void bindingFanoutExchange(Queue queue, FanoutExchange exchange) {
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareQueue(queue);
		Binding binding = BindingBuilder.bind(queue).to(exchange);
		rabbitAdmin.declareBinding(binding);
	}

	public void bindingTopicExchange(String queueKey, String exchangeKey, String routingKey) {
		TopicExchange exchange = new TopicExchange(exchangeKey, true, false);
		Queue queue = new Queue(queueKey, true);
		bindingTopicExchange(queue, exchange, routingKey);
	}

	public void bindingTopicExchange(Queue queue, TopicExchange exchange, String routingKey) {
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareQueue(queue);
		Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
		rabbitAdmin.declareBinding(binding);
	}
}
