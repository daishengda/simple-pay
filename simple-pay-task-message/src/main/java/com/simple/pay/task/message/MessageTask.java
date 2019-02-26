package com.simple.pay.task.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.simple.pay.task.message.service.IMessageScheduled;

@Component
public class MessageTask {

	private static final Log log = LogFactory.getLog(MessageTask.class);

	@Autowired
	private IMessageScheduled messageScheduled;

	/**
	 * 间隔一分钟跑一次任务
	 */
	private final static long ONE_MINUTE = 60 * 1000;

	/**
	 * 处理状态为“待确认”但已超时的消息的定时任务
	 */
	@Scheduled(fixedDelay = ONE_MINUTE)
	public void handleWaitingConfirmTimeOutJob() {
		log.info("执行(处理[waiting_confirm]状态的消息)任务开始");
		messageScheduled.handleWaitingConfirmTimeOutMessage();
		log.info("执行(处理[waiting_confirm]状态的消息)任务结束");
	}

	/**
	 * 处理状态为“发送中”但超时没有被成功消费确认的消息的定时任务
	 */
	@Scheduled(fixedDelay = ONE_MINUTE)
	public void handleSendingTimeOutJob() {
		log.info("执行(处理[SENDING]的消息)任务开始");
		messageScheduled.handleSendingTimeOutMessage();
		log.info("执行(处理[SENDING]的消息)任务结束");
	}
}
