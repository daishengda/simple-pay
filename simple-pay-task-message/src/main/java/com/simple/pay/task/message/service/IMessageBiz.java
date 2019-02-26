package com.simple.pay.task.message.service;

import java.util.Map;

import com.simple.pay.task.message.po.SpTransactionMessage;

/**
 * 消息业务处理接口
 * @author daishengda
 *
 */
public interface IMessageBiz {

	/**
	 *  处理[waiting_confirm]状态的消息
	 * @param messageMap
	 */
	void handleWaitingConfirmTimeOutMessage(Map<String, SpTransactionMessage> messageMap);
	
	/**
	 * 处理[SENDING]状态的消息
	 * @param messageMap
	 */
	void handleSendingTimeOutMessage(Map<String, SpTransactionMessage> messageMap);
}
