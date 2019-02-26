package com.simple.pay.service.message.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.pay.service.message.config.MessageConfig;
import com.simple.pay.service.message.dao.SpTransactionMessageMapper;
import com.simple.pay.service.message.enums.MessageStatusEnum;
import com.simple.pay.service.message.enums.PublicEnum;
import com.simple.pay.service.message.exceptions.MessageBizException;
import com.simple.pay.service.message.page.PageBean;
import com.simple.pay.service.message.page.PageParam;
import com.simple.pay.service.message.po.SpTransactionMessage;
import com.simple.pay.service.message.producer.RabbitSender;
import com.simple.pay.service.message.service.ISpTransactionMessageService;

/**
 * 服务接口service实现
 * 
 * @author daishengda
 *
 */
@Service("spTransactionMessageService")
public class SpTransactionMessageService implements ISpTransactionMessageService {

	private static final Log log = LogFactory.getLog(SpTransactionMessageService.class);

	@Autowired
	private SpTransactionMessageMapper mapper;

	@Autowired
	private MessageConfig config;
	
	@Autowired
	private RabbitSender rabbitSender;
	
	@Override
	public int saveMessageWaitingConfirm(SpTransactionMessage message) throws MessageBizException {
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}
		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}
		message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		return mapper.insert(message);
	}

	@Override
	public void confirmAndSendMessage(String messageId) throws MessageBizException {
		SpTransactionMessage message = getMessageByMessageId(messageId);
		message.setStatus(MessageStatusEnum.SENDING.name());
		mapper.update(message);
		
		//发送消息到MQ中间件
		sendMessage(message);
	}

	/**
	 * 发送消息到MQ中间件
	 * @param message 待发送消息
	 */
	private void sendMessage(SpTransactionMessage message) {
		String queueKey = message.getConsumerQueue();
		//使用配置默认的exchange和路由键
		String exchangeKey = config.getExchange();
		String routingKey = config.getRoutingkey();
		//如果没配置，取队列名作为路由键
		if(StringUtils.isEmpty(routingKey))
		{
			routingKey = queueKey;
		}
		//声明队列以及绑定
		rabbitSender.bindingTopicExchange(queueKey, exchangeKey, routingKey);
		
		// 发送消息到MQ中间件(发送的路由键=配置的路由键+队列名)
		rabbitSender.send(exchangeKey, routingKey.replaceAll("*", queueKey), message.getMessageBody().getBytes());
	}

	@Override
	public int saveAndSendMessage(SpTransactionMessage message) throws MessageBizException {
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}
		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setAreadlyDead(PublicEnum.NO.name());
		message.setMessageSendTimes(0);
		int result = mapper.insert(message);

		// 发送消息到MQ中间件
		sendMessage(message);
		return result;
	}

	@Override
	public void directSendMessage(SpTransactionMessage message) throws MessageBizException {
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}

		// 发送消息到MQ中间件
		sendMessage(message);
	}

	@Override
	public void reSendMessage(SpTransactionMessage message) throws MessageBizException {
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空 ");
		}
		message.addSendTimes();
		mapper.update(message);

		// 发送消息到MQ中间件
		sendMessage(message);
	}

	@Override
	public void reSendMessageByMessageId(String messageId) throws MessageBizException {
		SpTransactionMessage message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}

		int maxTimes = Integer.valueOf(config.getSend().getMaxTimes());
		if (message.getMessageSendTimes() >= maxTimes) {
			message.setAreadlyDead(PublicEnum.YES.name());
		}
		message.setMessageSendTimes(message.getMessageSendTimes() + 1);
		mapper.update(message);

		// 发送消息到MQ中间件
		sendMessage(message);
	}

	@Override
	public void setMessageToAreadlyDead(String messageId) throws MessageBizException {
		SpTransactionMessage message = getMessageByMessageId(messageId);
		if (message == null) {
			throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
		}
		message.setAreadlyDead(PublicEnum.YES.name());
		mapper.update(message);
	}

	@Override
	public SpTransactionMessage getMessageByMessageId(String messageId) throws MessageBizException {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("messageId", messageId);
		return mapper.getBy(paramMap);
	}

	@Override
	public void deleteMessageByMessageId(String messageId) throws MessageBizException {
		mapper.delete(messageId);
	}

	@Override
	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageBizException {
		log.info("==>reSendAllDeadMessageByQueueName");
		int numPerPage = 1000;
		if (batchSize > 0 && batchSize < 100) {
			numPerPage = 100;
		} else if (batchSize > 100 && batchSize < 5000) {
			numPerPage = batchSize;
		} else if (batchSize > 5000) {
			numPerPage = 5000;
		} else {
			numPerPage = 1000;
		}
		int pageNum = 1;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("consumerQueue", queueName);
		paramMap.put("areadlyDead", PublicEnum.YES.name());
		paramMap.put("listPageSortType", "ASC");

		Map<String, SpTransactionMessage> messageMap = new HashMap<String, SpTransactionMessage>();
		PageBean<SpTransactionMessage> pageBean = listPage(new PageParam(pageNum, numPerPage), paramMap);
		List<SpTransactionMessage> recordList = pageBean.getRecordList();
		if (CollectionUtils.isEmpty(recordList)) {
			log.info("==>recordList is empty");
			return;
		}
		int pageCount = pageBean.getTotalPage();
		for (SpTransactionMessage message : recordList) {
			messageMap.put(message.getMessageId(), message);
		}
		for (pageNum = 2; pageNum < pageCount; pageNum++) {
			pageBean = listPage(new PageParam(pageNum, numPerPage), paramMap);
			recordList = pageBean.getRecordList();
			if (CollectionUtils.isEmpty(recordList)) {
				break;
			}
			for (SpTransactionMessage message : recordList) {
				messageMap.put(message.getMessageId(), message);
			}
		}
		recordList = null;
		pageBean = null;
		for (Entry<String, SpTransactionMessage> entry : messageMap.entrySet()) {
			SpTransactionMessage message = entry.getValue();
			message.setMessageSendTimes(message.getMessageSendTimes()+1);
			mapper.update(message);
			
			//发送消息到MQ中间件
			sendMessage(message);
		}
	}

	@Override
	public PageBean<SpTransactionMessage> listPage(PageParam pageParam, Map<String, Object> paramMap) throws MessageBizException {
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }

        // 统计总记录数
        Long totalCount = mapper.listPageCount(paramMap);

        // 校验当前页数
        int currentPage = PageBean.checkCurrentPage(totalCount.intValue(), pageParam.getNumPerPage(), pageParam.getPageNum());
        pageParam.setPageNum(currentPage); // 为当前页重新设值
        // 校验页面输入的每页记录数numPerPage是否合法
        int numPerPage = PageBean.checkNumPerPage(pageParam.getNumPerPage()); // 校验每页记录数
        pageParam.setNumPerPage(numPerPage); // 重新设值

        // 根据页面传来的分页参数构造SQL分页参数
        paramMap.put("pageFirst", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
        paramMap.put("pageSize", pageParam.getNumPerPage());
        paramMap.put("startRowNum", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
        paramMap.put("endRowNum", pageParam.getPageNum() * pageParam.getNumPerPage());

        // 获取分页数据集
        List<SpTransactionMessage> list = mapper.listPage(paramMap);

        // 构造分页对象
        return new PageBean<SpTransactionMessage>(pageParam.getPageNum(), pageParam.getNumPerPage(), totalCount.intValue(), list);
	}

}
