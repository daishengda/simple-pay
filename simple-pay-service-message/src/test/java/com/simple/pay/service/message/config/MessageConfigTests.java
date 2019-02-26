package com.simple.pay.service.message.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.pay.service.message.PayServiceMessageApplicationTests;
import com.simple.pay.service.message.config.MessageConfig.SendConfig;

/**
 * 消息配置测试类
 * @author daishengda
 *
 */
@Component
public class MessageConfigTests extends PayServiceMessageApplicationTests {

	@Autowired
	private MessageConfig config;
	
	@Test
	public void parseMessageConfigTest()
	{
		SendConfig send = config.getSend();
//		int[] intervalTime = send.getIntervalTime();
//		int maxTimes = send.getMaxTimes();
//		int timeOut = send.getTimeOut();
		Assert.assertTrue(send != null);
	}
}
