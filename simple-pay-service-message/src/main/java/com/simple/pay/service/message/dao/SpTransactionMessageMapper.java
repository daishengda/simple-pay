package com.simple.pay.service.message.dao;

import java.util.List;
import java.util.Map;

import com.simple.pay.service.message.po.SpTransactionMessage;

public interface SpTransactionMessageMapper extends BaseMapper<SpTransactionMessage>{

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
    List<SpTransactionMessage> listPage(Map<String, Object> paramMap);
}