package com.simple.pay.service.notify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通知配置参数信息
 * 
 * @author daishengda
 *
 */
@Component
@ConfigurationProperties(prefix = "notify")
public class NotifyConfig {

	private SendConfig send;
	
	private String exchange;
	
	private String routingkey;
	
	private String queueKey;

	public SendConfig getSend() {
		return send;
	}

	public void setSend(SendConfig send) {
		this.send = send;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingkey() {
		return routingkey;
	}

	public void setRoutingkey(String routingkey) {
		this.routingkey = routingkey;
	}

	public String getQueueKey() {
		return queueKey;
	}

	public void setQueueKey(String queueKey) {
		this.queueKey = queueKey;
	}

	public static class SendConfig {

		/**
		 * 消息间隔发送时间 时间单位分
		 */
		private int[] intervalTime;

		public int[] getIntervalTime() {
			return intervalTime;
		}

		public void setIntervalTime(int[] intervalTime) {
			this.intervalTime = intervalTime;
		}

	}
}
