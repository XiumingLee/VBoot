package cn.xiuminglee.vboot.core.common.exception;

import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Xiuming Lee
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalDefultExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * //声明要捕获的异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public SimpleResponse defultExcepitonHandler(HttpServletRequest request, Exception e) {
        /** 异常的具体信息*/
        String details = VBootUtils.stackTraceElementToString(e.getStackTrace());
        logger.error("全局异常捕获：",e);
        VBootUtils.errLogSave(new Log("自定义/抛出异常！",999,e.getMessage(),details,""));
        return new SimpleResponse(999,e.getMessage(),e.getStackTrace());
    }
}
