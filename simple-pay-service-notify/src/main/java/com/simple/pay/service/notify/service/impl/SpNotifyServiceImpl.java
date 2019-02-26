package com.simple.pay.service.notify.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.pay.service.notify.config.NotifyConfig;
import com.simple.pay.service.notify.dao.SpNotifyRecordLogMapper;
import com.simple.pay.service.notify.dao.SpNotifyRecordMapper;
import com.simple.pay.service.notify.entity.SpNotifyRecord;
import com.simple.pay.service.notify.entity.SpNotifyRecordLog;
import com.simple.pay.service.notify.enums.NotifyStatusEnum;
import com.simple.pay.service.notify.enums.NotifyTypeEnum;
import com.simple.pay.service.notify.exceptions.NotifyBizException;
import com.simple.pay.service.notify.page.PageBean;
import com.simple.pay.service.notify.page.PageParam;
import com.simple.pay.service.notify.producer.RabbitSender;
import com.simple.pay.service.notify.service.ISpNotifyService;
import com.simple.pay.service.notify.utils.GSONUtil;

/**
 * 通知service实现类
 * @author daishengda
 *
 */
@Service("spNotifyService")
public class SpNotifyServiceImpl implements ISpNotifyService {
	
	@Autowired
	private RabbitSender rabbitSender;
	
	@Autowired
	private NotifyConfig config;

	@Autowired
	private SpNotifyRecordMapper recordMapper;
	
	@Autowired
	private SpNotifyRecordLogMapper recordLogMapper;
	
	@Override
	public long createNotifyRecord(SpNotifyRecord spNotifyRecord) throws NotifyBizException {
		return recordMapper.insert(spNotifyRecord);
	}

	@Override
	public void updateNotifyRecord(SpNotifyRecord spNotifyRecord) throws NotifyBizException {
		recordMapper.update(spNotifyRecord);
	}

	@Override
	public long createNotifyRecordLog(SpNotifyRecordLog spNotifyRecordLog) throws NotifyBizException {
		return recordLogMapper.insert(spNotifyRecordLog);
	}

	@Override
	public void notifySend(String notifyUrl, String merchantOrderNo, String merchantNo) throws NotifyBizException {
		SpNotifyRecord record = new SpNotifyRecord();
		record.setNotifyTimes(0);
		record.setLimitNotifyTimes(5);
		record.setStatus(NotifyStatusEnum.CREATED.name());
		record.setUrl(notifyUrl);
		record.setMerchantOrderNo(merchantOrderNo);
		record.setMerchantNo(merchantNo);
		record.setNotifyType(NotifyTypeEnum.MERCHANT.name());
		String body = GSONUtil.toJson(record);
		
		//发送消息到消息中间件
		sendMessage(body);
	}

	@Override
	public SpNotifyRecord getNotifyRecordById(String id) throws NotifyBizException {
		return recordMapper.selectById(id);
	}

	@Override
	public SpNotifyRecord getNotifyByMerchantNoAndOrderNoAndNotifyType(String merchantNo, String merchantOrderNo,
			String notifyType) throws NotifyBizException {
		return recordMapper.getNotifyByMerchantNoAndOrderNoAndNotifyType(merchantNo, merchantOrderNo, notifyType);
	}

	@Override
	public PageBean<SpNotifyRecord> queryNotifyRecordListPage(PageParam pageParam, Map<String, Object> paramMap)
			throws NotifyBizException {
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }

        // 统计总记录数
        Long totalCount = recordMapper.listPageCount(paramMap);

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
        List<SpNotifyRecord> list = recordMapper.listPage(paramMap);

        // 构造分页对象
        return new PageBean<SpNotifyRecord>(pageParam.getPageNum(), pageParam.getNumPerPage(), totalCount.intValue(), list);
	}

	/**
	 * 发送消息到MQ中间件
	 * @param body 待发送消息
	 */
	private void sendMessage(String body) {
		String queueKey = config.getQueueKey();
		//使用配置默认的exchange和路由键
		String exchangeKey = config.getExchange();
		String routingKey = config.getRoutingkey();
		//声明队列以及绑定
		rabbitSender.bindingTopicExchange(queueKey, exchangeKey, routingKey);
		
		// 发送消息到MQ中间件(发送的路由键=配置的路由键+队列名)
		rabbitSender.send(exchangeKey, routingKey.replaceAll("*", queueKey), body.getBytes());
	}
}
