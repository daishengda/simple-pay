package com.simple.pay.service.message.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.pay.service.message.PayServiceMessageApplicationTests;
import com.simple.pay.service.message.enums.MessageStatusEnum;
import com.simple.pay.service.message.enums.PublicEnum;
import com.simple.pay.service.message.po.SpTransactionMessage;

/**
 * dao层测试类
 * @author daishengda
 *
 */
@Component
public class SpTransactionMessageMapperTest extends PayServiceMessageApplicationTests {

	private SpTransactionMessage message;
	
    @Autowired
    private SpTransactionMessageMapper mapper;
    
    @Before
    public void initConfig()
    {
    	 message = constructionObject();
    }

	private SpTransactionMessage constructionObject() {
		SpTransactionMessage message = new SpTransactionMessage();
    	message.setId("test1234567890");
    	message.setVersion(0);
    	message.setCreater("admin");
    	message.setEditor("admin");
    	message.setMessageId("MSG123456");
    	message.setMessageBody("{title:hello}");
    	message.setMessageDataType("JSON");
    	message.setConsumerQueue("pay10086");
    	message.setStatus(MessageStatusEnum.SENDING.name());
    	message.setAreadlyDead(PublicEnum.NO.name());
    	message.setMessageSendTimes(1);
    	message.setRemark("测试单条插入数据");
		return message;
	}
    
    @Test
    public void testInsertObject() {
    	
    	int row = mapper.insert(message);
        Assert.assertTrue(row>0);
    }

    @Test
    public void testUpdateObject() {
    	message.setEditor("daishengda");
    	message.setAreadlyDead(PublicEnum.YES.name());
    	message.setRemark("测试修改数据接口");
    	int row = mapper.update(message);
        Assert.assertTrue(row>0);
    }
    
    
    @Test
    public void testSelectById() {
    	SpTransactionMessage row = mapper.selectById(message.getId());
        Assert.assertTrue(row != null);
    }
    
    @Test
    public void testListBy()
    {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("areadlyDead", PublicEnum.YES.name());
    	List<SpTransactionMessage> listBy = mapper.listBy(paramMap);
    	Assert.assertTrue(listBy.size() > 0);
    }
    
    @Test
    public void testDelete()
    {
    	String messageId = "MSG123456";
    	int deleteNum = mapper.delete(messageId);
    	Assert.assertTrue(deleteNum > 0);
    }
}