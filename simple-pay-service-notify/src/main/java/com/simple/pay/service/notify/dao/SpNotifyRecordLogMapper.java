package com.simple.pay.service.notify.dao;

import com.simple.pay.service.notify.entity.SpNotifyRecordLog;

/**
 * 通知记录的日志数据表DAO层接口
 * @author daishengda
 *
 */
public interface SpNotifyRecordLogMapper extends BaseMapper<SpNotifyRecordLog>{

	/**
	 * 根据id删除数据
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(String id);
	
	/**
	 * 根据id查询通知记录的日志
	 * @param id
	 * @return
	 */
	SpNotifyRecordLog selectByPrimaryKey(String id);

	/**
	 * 更新消息通知记录的日志数据
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(SpNotifyRecordLog record);
}
