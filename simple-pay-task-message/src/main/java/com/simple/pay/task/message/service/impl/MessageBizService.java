package com.simple.pay.task.message.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.simple.pay.task.message.config.MessageConfig;
import com.simple.pay.task.message.po.SpTransactionMessage;
import com.simple.pay.task.message.service.IMessageBiz;

/**
 * 消息业务处理实现类
 * @author daishengda
 *
 */
public class MessageBizService implements IMessageBiz{
	
	
	private static final Log log = LogFactory.getLog(MessageBizService.class);

	@Autowired
	private MessageConfig messageConfig;
	
	@Override
	public void handleWaitingConfirmTimeOutMessage(Map<String, SpTransactionMessage> messageMap) {
		log.debug("开始处理[waiting_confirm]状态的消息,总条数[" + messageMap.size() + "]");
		// 单条消息处理（目前该状态的消息，消费队列全部是accounting，如果后期有业务扩充，需做队列判断，做对应的业务处理。）
		for (Entry<String, SpTransactionMessage> entry : messageMap.entrySet()) {
			SpTransactionMessage message = entry.getValue();
			log.debug("开始处理[waiting_confirm]消息ID为[" + message.getMessageId() + "]的消息");
			String bankOrderNo = message.getField1();
			//如果订单成功，把消息改为待处理，并发送消息(后续完善)
			if(bankOrderNo.startsWith("111"))
			{
				// 确认并发送消息
//				rpTransactionMessageService.confirmAndSendMessage(message.getMessageId());
			} else if(bankOrderNo.startsWith("2222"))
			{
				// 订单状态是等待支付，可以直接删除数据
				log.debug("订单没有支付成功,删除[waiting_confirm]消息id[" + message.getMessageId() + "]的消息");
//				rpTransactionMessageService.deleteMessageByMessageId(message.getMessageId());
			}
		}
	}

	@Override
	public void handleSendingTimeOutMessage(Map<String, SpTransactionMessage> messageMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.debug("开始处理[SENDING]状态的消息,总条数[" + messageMap.size() + "]");

		// 根据配置获取通知间隔时间
		Map<Integer, Integer> notifyParam = getSendTime();
		
		// 单条消息处理
		for (Entry<String, SpTransactionMessage> entry : messageMap.entrySet()) {
			SpTransactionMessage message = entry.getValue();
			try {
				log.debug("开始处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息");
				//	判断发送次数
				int maxTimes = messageConfig.getSend().getMaxTimes();
				log.debug("[SENDING]消息ID为[" + message.getMessageId() + "]的消息,已经重新发送的次数[" + message.getMessageSendTimes() + "]");
				//	如果超过最大发送次数直接退出
				if(maxTimes<message.getMessageSendTimes())
				{
					// 标记为死亡
					//rpTransactionMessageService.setMessageToAreadlyDead(message.getMessageId());
					continue;
				}
				//	判断是否达到发送消息的时间间隔条件
				int reSendTimes = message.getMessageSendTimes();
				int times = notifyParam.get(reSendTimes == 0 ? 1 : reSendTimes);
				long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
				long needTime = currentTimeInMillis - times * 60 * 1000;
				long hasTime = message.getEditTime().getTime();
				//	判断是否达到了可以再次发送的时间条件
				if(hasTime>needTime)
				{
					log.debug("currentTime[" + sdf.format(new Date()) + "],[SENDING]消息上次发送时间[" + sdf.format(message.getEditTime()) + "],必须过了[" + times + "]分钟才可以再发送。");
					continue;
				}
				
				//重新发送消息
//				rpTransactionMessageService.reSendMessage(message);

				log.debug("结束处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息");
			} catch (Exception e) {
				log.error("处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息异常：", e);
			}
		}
	}


	/**
	 * 根据配置获取通知间隔时间
	 * 
	 * @return
	 */
	private Map<Integer, Integer> getSendTime() {
		Map<Integer, Integer> notifyParam = new HashMap<Integer, Integer>();
		int[] intervalTimes = messageConfig.getSend().getIntervalTime();
		for (int i = 0; i < intervalTimes.length; i++) {
			int j = intervalTimes[i];
			notifyParam.put(i, j);
		}
		return notifyParam;
	}
}
