package com.simple.pay.task.message.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.pay.task.message.config.MessageConfig;
import com.simple.pay.task.message.enums.MessageStatusEnum;
import com.simple.pay.task.message.enums.PublicEnum;
import com.simple.pay.task.message.po.SpTransactionMessage;
import com.simple.pay.task.message.service.IMessageBiz;
import com.simple.pay.task.message.service.IMessageScheduled;

/**
 * 消息定时器实现类
 * 
 * @author daishengda
 *
 */
@Service("MessageScheduledService")
public class MessageScheduledService implements IMessageScheduled {

	private static final Log log = LogFactory.getLog(MessageScheduledService.class);

	@Autowired
	private IMessageBiz messageBiz;
	
	@Autowired
	private MessageConfig messageConfig;
	
	@Override
	public void handleWaitingConfirmTimeOutMessage() {
		try {
			int numPerPage = 2000; // 每页条数
			int maxHandlePageCount = 3; //一次最多处理页数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("listPageSortType", "ASC");
			//获取配置的开始处理时间
			String dateStr = getCreateTimeBefore();
			paramMap.put("createTimeBefore", dateStr);// 取存放了多久的消息
			paramMap.put("status", MessageStatusEnum.WAITING_CONFIRM.name());// 取状态为“待确认”的消息
			Map<String, SpTransactionMessage> messageMap = getMessageMap(numPerPage, maxHandlePageCount, paramMap);
			messageBiz.handleWaitingConfirmTimeOutMessage(messageMap);		
		} catch (Exception e) {
			log.error("处理[waiting_confirm]状态的消息异常" + e);
		}
	}
	

	@Override
	public void handleSendingTimeOutMessage() {
		try {
			int numPerPage = 2000;	// 每页条数
			int maxHandlePageCount = 3;	//一次最多处理页数
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			//paramMap.put("consumerQueue", queueName); // 队列名（可以按不同业务队列分开处理）
			paramMap.put("listPageSortType", "ASC"); // 分页查询的排序方式，正向排序
			// 获取配置的开始处理的时间
			String dateStr = getCreateTimeBefore();
			paramMap.put("createTimeBefore", dateStr);// 取存放了多久的消息
			paramMap.put("status", MessageStatusEnum.SENDING.name());// 取状态为发送中的消息
			paramMap.put("areadlyDead", PublicEnum.NO.name());// 取存活的发送中消息
			Map<String, SpTransactionMessage> messageMap = getMessageMap(numPerPage, maxHandlePageCount, paramMap);
			messageBiz.handleSendingTimeOutMessage(messageMap);
		} catch (Exception e) {
			log.error("处理发送中的消息异常" + e);
		}
	}
	
	/**
	 * 根据分页参数及查询条件批量获取消息数据.
	 * @param numPerPage 每页记录数.
	 * @param maxHandlePageCount 最多获取页数.
	 * @param paramMap 查询参数.
	 * @return 
	 */
	private Map<String, SpTransactionMessage> getMessageMap(int numPerPage, int maxHandlePageCount, Map<String, Object> paramMap){
		return null;
	}
	
	/**
	 * 获取配置的开始处理的时间
	 * 
	 * @return
	 */
	private String getCreateTimeBefore() {
		long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
		Date date = new Date(currentTimeInMillis - messageConfig.getSend().getTimeOut() * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}
}
