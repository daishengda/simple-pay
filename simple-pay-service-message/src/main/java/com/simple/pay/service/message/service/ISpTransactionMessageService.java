package com.simple.pay.service.message.service;

import java.util.Map;

import com.simple.pay.service.message.exceptions.MessageBizException;
import com.simple.pay.service.message.page.PageBean;
import com.simple.pay.service.message.page.PageParam;
import com.simple.pay.service.message.po.SpTransactionMessage;

/**
 * 消息服务service接口
 * @author daishengda
 *
 */
public interface ISpTransactionMessageService {

	/**
	 * 预存储消息
	 * @param spTransactionMessage 消息实体类
	 * @return
	 * @throws MessageBizException
	 */
	int saveMessageWaitingConfirm(SpTransactionMessage spTransactionMessage) throws MessageBizException;
	
	/**
	 * 确认并发送消息
	 * @param messageId 消息ID
	 * @throws MessageBizException
	 */
	void confirmAndSendMessage(String messageId) throws MessageBizException;
	
	/**
	 *   存储并发送消息
	 * @param spTransactionMessage 消息实体类
	 * @return
	 * @throws MessageBizException
	 */
	int saveAndSendMessage(SpTransactionMessage spTransactionMessage) throws MessageBizException;

	/**
	 *  直接发送消息
	 * @param spTransactionMessage 消息实体类
	 * @throws MessageBizException
	 */
	void directSendMessage(SpTransactionMessage spTransactionMessage) throws MessageBizException;
	
	/**
	 * 重发消息
	 * @param spTransactionMessage 消息实体类
	 * @throws MessageBizException
	 */
	void reSendMessage(SpTransactionMessage spTransactionMessage) throws MessageBizException;
	
	/**
	 *  根据messageId重发某条消息
	 * @param messageId 消息ID
	 * @throws MessageBizException
	 */
	void reSendMessageByMessageId(String messageId) throws MessageBizException;
	
	/**
	 * 将消息标记为死亡消息
	 * @param messageId 消息ID
	 * @throws MessageBizException
	 */
	void setMessageToAreadlyDead(String messageId) throws MessageBizException;
	
	/**
	 * 根据消息ID获取消息
	 * @param messageId 消息ID
	 * @return
	 * @throws MessageBizException
	 */
	SpTransactionMessage getMessageByMessageId(String messageId) throws MessageBizException;

	/**
	 * 根据消息ID删除消息
	 * @param messageId 消息ID
	 * @throws MessageBizException
	 */
	void deleteMessageByMessageId(String messageId) throws MessageBizException;
	
	/**
	 * 重发某个消息队列中的全部已死亡的消息
	 * @param queueName 队列名称
	 * @param batchSize 批量插入的数量
	 * @throws MessageBizException
	 */
	void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageBizException;

	/**
	 * 获取分页数据
	 * @param pageParam
	 * @param paramMap
	 * @return
	 * @throws MessageBizException
	 */
	PageBean<SpTransactionMessage> listPage(PageParam pageParam, Map<String, Object> paramMap) throws MessageBizException;
}
