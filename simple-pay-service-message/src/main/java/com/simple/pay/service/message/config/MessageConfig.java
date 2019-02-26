package com.simple.pay.service.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息配置参数信息
 * 
 * @author daishengda
 *
 */
@Component
@ConfigurationProperties(prefix = "message")
public class MessageConfig {

	private SendConfig send;
	
	private String exchange;
	
	private String routingkey;

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

	public static class SendConfig {
		/**
		 * 最大发送次数次数
		 */
		private int maxTimes;

		/**
		 * message timeout (消息存放超过下面设置的时间才可以取出处理),单位：秒
		 */
		private int timeOut;

		/**
		 * 消息间隔发送时间 时间单位分
		 */
		private int[] intervalTime;

		public int getMaxTimes() {
			return maxTimes;
		}

		public void setMaxTimes(int maxTimes) {
			this.maxTimes = maxTimes;
		}

		public int getTimeOut() {
			return timeOut;
		}

		public void setTimeOut(int timeOut) {
			this.timeOut = timeOut;
		}

		public int[] getIntervalTime() {
			return intervalTime;
		}

		public void setIntervalTime(int[] intervalTime) {
			this.intervalTime = intervalTime;
		}

	}
}
