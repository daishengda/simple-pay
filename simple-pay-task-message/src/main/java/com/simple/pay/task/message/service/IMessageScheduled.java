package com.simple.pay.task.message.service;

/**
 * 消息定时器接口
 * @author daishengda
 *
 */
public interface IMessageScheduled {

	/**
	 * 处理状态为“待确认”但已超时的消息.
	 */
	void handleWaitingConfirmTimeOutMessage();
	
	/**
	 * 处理状态为“发送中”但超时没有被成功消费确认的消息
	 */
	void handleSendingTimeOutMessage();
}
