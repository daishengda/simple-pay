package com.simple.pay.service.notify.service;

import java.util.Map;

import com.simple.pay.service.notify.entity.SpNotifyRecord;
import com.simple.pay.service.notify.entity.SpNotifyRecordLog;
import com.simple.pay.service.notify.exceptions.NotifyBizException;
import com.simple.pay.service.notify.page.PageBean;
import com.simple.pay.service.notify.page.PageParam;

/**
 * 通知service接口
 * @author daishengda
 *
 */
public interface ISpNotifyService {

	/**
	 * 创建消息通知
	 * @param spNotifyRecord 
	 * @return
	 * @throws NotifyBizException
	 */
	long createNotifyRecord(SpNotifyRecord spNotifyRecord) throws NotifyBizException;

	/**
	 * 修改消息通知
	 * @param spNotifyRecord
	 * @throws NotifyBizException
	 */
	void updateNotifyRecord(SpNotifyRecord spNotifyRecord) throws NotifyBizException;

	/**
	 * 创建消息通知记录的日志
	 * @param spNotifyRecordLog
	 * @return
	 * @throws NotifyBizException
	 */
	long createNotifyRecordLog(SpNotifyRecordLog spNotifyRecordLog) throws NotifyBizException;

	/**
	 * 发送消息通知
	 * @param notifyUrl 通知地址
	 * @param merchantOrderNo 商户订单号
	 * @param merchantNo 商户编号
	 * @throws NotifyBizException
	 */
	void notifySend(String notifyUrl, String merchantOrderNo, String merchantNo) throws NotifyBizException;
	
	/**
	 * 通过ID获取通知记录
	 * @param id
	 * @return
	 * @throws NotifyBizException
	 */
	SpNotifyRecord getNotifyRecordById(String id) throws NotifyBizException;

	/**
	 * 根据商户编号,商户订单号,通知类型获取通知记录
	 * @param merchantNo 商户编号
	 * @param merchantOrderNo 商户订单号
	 * @param notifyType 消息类型
	 * @return
	 * @throws NotifyBizException
	 */
	SpNotifyRecord getNotifyByMerchantNoAndOrderNoAndNotifyType(String merchantNo , String merchantOrderNo , String notifyType) throws NotifyBizException;

	/**
	 * 按条件分页查询通知记录
	 * @param pageParam
	 * @param paramMap
	 * @return
	 * @throws NotifyBizException
	 */
	PageBean<SpNotifyRecord> queryNotifyRecordListPage(PageParam pageParam , Map<String, Object> paramMap) throws NotifyBizException;
}
