package com.simple.pay.service.notify.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 通知业务异常类
 * @author daishengda
 *
 */
public class NotifyBizException extends BizException {

    /**
     *
     */
    private static final long serialVersionUID = 3536909333010163563L;

    /** 保存的消息为空 **/
    public static final int NOTIFY_SYSTEM_EXCEPTION = 9001;

    private static final Log LOG = LogFactory.getLog(NotifyBizException.class);

    public NotifyBizException() {
    }

    public NotifyBizException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public NotifyBizException(int code, String msg) {
        super(code, msg);
    }

    public NotifyBizException print() {
        LOG.info("==>BizException, code:" + this.code + ", msg:" + this.msg);
        return this;
    }
}
