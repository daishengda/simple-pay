package com.simple.pay.app.notify.entity;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.simple.pay.app.notify.config.NotifyConfig;
import com.simple.pay.app.notify.exceptions.BizException;

/**
 * 通知任务类
 * @author daishengda
 *
 */
public class NotifyTask implements Runnable, Delayed{

    private static final Log LOG = LogFactory.getLog(NotifyTask.class);
	
    private long executeTime;
    
    private SpNotifyRecord notifyRecord;
    
    private NotifyConfig notifyConfig;
    
	@Override
	public int compareTo(Delayed o) {
		NotifyTask task = (NotifyTask) o;
		return executeTime > task.executeTime ? 1 : (executeTime < task.executeTime ? -1 : 0);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
        Integer notifyTimes = notifyRecord.getNotifyTimes(); // 得到当前通知对象的通知次数
        Integer maxNotifyTimes = notifyRecord.getLimitNotifyTimes(); // 最大通知次数
        Date notifyTime = new Date(); // 本次通知的时间
        
        // 开始通知
        try {
            LOG.info("===>notify url " + notifyRecord.getUrl()+", notify id:" + notifyRecord.getId()+", notifyTimes:" + notifyTimes);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> result = restTemplate.postForEntity(notifyRecord.getUrl(), null, String.class);
            notifyRecord.setNotifyTimes(notifyTimes + 1);
            
            String successValue = notifyConfig.getSuccessValue();
            String responseMsg = "";
            HttpStatus httpStatus = result.getStatusCode();
            
            //写通知日志表
//            notifyPersist.saveNotifyRecordLogs(notifyRecord.getId(), notifyRecord.getMerchantNo(), notifyRecord.getMerchantOrderNo(), notifyRecord.getUrl(), responseMsg, responseStatus);
            LOG.info("===>insert NotifyRecordLog, merchantNo:" + notifyRecord.getMerchantNo() + ", merchantOrderNo:" + notifyRecord.getMerchantOrderNo());
            switch (httpStatus) {
			case OK:
			case CREATED:
			case ACCEPTED:
			case NON_AUTHORITATIVE_INFORMATION:
			case NO_CONTENT:
			case RESET_CONTENT:
			case PARTIAL_CONTENT:
                responseMsg = result.getBody().trim();
                responseMsg = responseMsg.length() >= 600 ? responseMsg.substring(0, 600) : responseMsg; // 避免异常日志过长
                LOG.info("===>订单号: " + notifyRecord.getMerchantOrderNo() + " HTTP_STATUS:" + httpStatus.value() + ",请求返回信息：" + responseMsg);
                
                // 通知成功,更新通知记录为已通知成功（以后不再通知）
                if (responseMsg.trim().equals(successValue)) {
//                    notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.SUCCESS.name(), notifyTime);
                    return;
                }
                // 通知不成功(返回的结果不是success)
                if(notifyRecord.getNotifyTimes() < maxNotifyTimes)
                {
                	// 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
//                	notifyQueue.addToNotifyTaskDelayQueue(notifyRecord);
//                	notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_SUCCESS.name(), notifyTime);
                	LOG.info("===>update NotifyRecord status to HTTP_REQUEST_SUCCESS, notifyId:" + notifyRecord.getId());
                }
                else
                {
                	// 到达最大通知次数限制，标记为通知失败
//                	notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.FAILED.name(), notifyTime);
                	LOG.info("===>update NotifyRecord status to failed, notifyId:" + notifyRecord.getId());
                }
                break;

			default:
            	// 其它HTTP响应状态码情况下
            	if (notifyRecord.getNotifyTimes() < maxNotifyTimes) {
                	// 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
//                	notifyQueue.addToNotifyTaskDelayQueue(notifyRecord);
//                	notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_FALIED.name(), notifyTime);
                	LOG.info("===>update NotifyRecord status to HTTP_REQUEST_FALIED, notifyId:" + notifyRecord.getId());
                }else{
                	// 到达最大通知次数限制，标记为通知失败
//                	notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.FAILED.name(), notifyTime);
                	LOG.info("===>update NotifyRecord status to failed, notifyId:" + notifyRecord.getId());
                }
				break;
			}
		} catch (BizException e) {
			LOG.error("===>NotifyTask", e);
		} catch (Exception e) {
        	// 异常
            LOG.error("===>NotifyTask", e);
//            notifyQueue.addToNotifyTaskDelayQueue(notifyRecord); // 判断是否超过重发次数，未超重发次数的，再次进入延迟发送队列
//            notifyPersist.updateNotifyRord(notifyRecord.getId(), notifyRecord.getNotifyTimes(), NotifyStatusEnum.HTTP_REQUEST_FALIED.name(), notifyTime);
//            notifyPersist.saveNotifyRecordLogs(notifyRecord.getId(), notifyRecord.getMerchantNo(), notifyRecord.getMerchantOrderNo(), notifyRecord.getUrl(), "", 0);
        }
	}

	
}
