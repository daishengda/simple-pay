package com.simple.pay.service.notify.entity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.simple.pay.service.notify.enums.NotifyStatusEnum;
import com.simple.pay.service.notify.enums.NotifyTypeEnum;
import com.simple.pay.service.notify.utils.GSONUtil;

/**
 * 通知记录实体
 * @author daishengda
 *
 */
public class SpNotifyRecord  extends BaseEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1298961428368118378L;

	private Date createTime;
    
    /** 通知规则 */
    private String notifyRule;

    /** 最后一次通知时间 **/
    private Date lastNotifyTime;

    /** 通知次数 **/
    private Integer notifyTimes;

    /** 限制通知次数 **/
    private Integer limitNotifyTimes;

    /** 通知URL **/
    private String url;

    /** 商户编号 **/
    private String merchantNo;

    /** 商户订单号 **/
    private String merchantOrderNo;

    /** 通知类型 NotifyTypeEnum **/
    private String notifyType;

    public SpNotifyRecord() {
        super();
    }

    public SpNotifyRecord(Date createTime, String notifyRule, Date lastNotifyTime, Integer notifyTimes, Integer limitNotifyTimes, String url, String merchantNo,
                          String merchantOrderNo, NotifyStatusEnum status, NotifyTypeEnum type) {
        super();
        this.createTime = createTime;
        this.notifyRule = notifyRule;
        this.lastNotifyTime = lastNotifyTime;
        this.notifyTimes = notifyTimes;
        this.limitNotifyTimes = limitNotifyTimes;
        this.url = url;
        this.merchantNo = merchantNo;
        this.merchantOrderNo = merchantOrderNo;
        this.notifyType = type.name();
        super.setStatus(status.name());
    }


    
    /** 通知规则 */
    public String getNotifyRule() {
		return notifyRule;
	}

    /** 通知规则 */
	public void setNotifyRule(String notifyRule) {
		this.notifyRule = notifyRule;
	}
	
	/**
	 * 获取通知规则的Map<String, Integer>.
	 * @return
	 */
	public Map<String, Integer> getNotifyRuleMap(){
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		return GSONUtil.fromJson(getNotifyRule(), type);
	}

	/** 最后一次通知时间 **/
    public Date getLastNotifyTime() {
        return lastNotifyTime;
    }

    /** 最后一次通知时间 **/
    public void setLastNotifyTime(Date lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

    /** 通知次数 **/
    public Integer getNotifyTimes() {
        return notifyTimes;
    }

    /** 通知次数 **/
    public void setNotifyTimes(Integer notifyTimes) {
        this.notifyTimes = notifyTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 限制通知次数 **/
    public Integer getLimitNotifyTimes() {
        return limitNotifyTimes;
    }

    /** 限制通知次数 **/
    public void setLimitNotifyTimes(Integer limitNotifyTimes) {
        this.limitNotifyTimes = limitNotifyTimes;
    }

    /** 通知URL **/
    public String getUrl() {
        return url;
    }

    /** 通知URL **/
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /** 商户编号 **/
    public String getMerchantNo() {
        return merchantNo;
    }

    /** 商户编号 **/
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }

    /** 商户订单号 **/
    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    /** 商户订单号 **/
    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo == null ? null : merchantOrderNo.trim();
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

}
