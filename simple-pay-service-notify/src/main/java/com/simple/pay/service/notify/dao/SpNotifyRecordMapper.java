package com.simple.pay.service.notify.dao;

import java.util.List;
import java.util.Map;

import com.simple.pay.service.notify.entity.SpNotifyRecord;

/**
 * 通知记录数据表DAO层接口
 * @author daishengda
 *
 */
public interface SpNotifyRecordMapper extends BaseMapper<SpNotifyRecord>{

	/**
	 * 根据商户编号,商户订单号,通知类型获取通知记录
	 * @param merchantNo 商户编号
	 * @param merchantOrderNo 商户订单号
	 * @param notifyType 消息类型
	 * @return
	 */
	SpNotifyRecord getNotifyByMerchantNoAndOrderNoAndNotifyType(String merchantNo, String merchantOrderNo, String notifyType);

	/**
	 * 根据id删除数据
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(String id);
	
	/**
	 * 根据id查询通知记录
	 * @param id
	 * @return
	 */
	SpNotifyRecord selectByPrimaryKey(String id);

	/**
	 * 更新消息通知记录数据
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(SpNotifyRecord record);
	
    /**
     * 根据column查询记录数.
     * @param paramMap
     * @return
     */
    Long listPageCount(Map<String, Object> paramMap);
    
    /**
     * 根据分页参数查询数据
     * @param paramMap
     * @return
     */
    List<SpNotifyRecord> listPage(Map<String, Object> paramMap);
}
