package com.simple.pay.app.notify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通知配置信息
 * @author daishengda
 *
 */
@Component
@ConfigurationProperties(prefix = "notify")
public class NotifyConfig {

	/**
	 * 通知参数（通知规则）
	 */
	private int[] intervalTime;
	
    /**
     * 通知后用于判断是否成功的返回值（成功标识）,由HttpResponse获取
     */
    private String successValue;

	public int[] getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int[] intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getSuccessValue() {
		return successValue;
	}

	public void setSuccessValue(String successValue) {
		this.successValue = successValue;
	}
}
