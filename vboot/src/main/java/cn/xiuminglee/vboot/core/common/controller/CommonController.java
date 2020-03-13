package cn.xiuminglee.vboot.core.common.controller;

import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @author 22
 */
@RestController
public class CommonController extends AbstractErrorController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public CommonController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @GetMapping("/loginMust")
    public SimpleResponse loginMust(){
        return new SimpleResponse(401,"请先登录！");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * web错误统一拦截controller
     * @param webRequest
     * @return
     */
    @RequestMapping("${server.error.path:${error.path:/error}}")
    public SimpleResponse globalError(ServletWebRequest webRequest){
        //调用父类方法获取错误信息
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest.getRequest(), true);
        String method = webRequest.getRequest().getMethod();
        VBootUtils.errLogSave(new Log("业务代码错误日志！",(Integer) errorAttributes.get("status"),(String) errorAttributes.get("message"),(String) errorAttributes.get("trace"),method+">>"+errorAttributes.get("path")));
        return new SimpleResponse(999,(String) errorAttributes.get("message"),errorAttributes);
    }

    /**
     * 供前端请求验证是否登录。
     * @return
     */
    @GetMapping("/isLogin")
    public SimpleResponse isLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(String.valueOf(authentication));
        //判断是不是登录
        if (authentication.getName().equals("anonymousUser")){
            return new SimpleResponse(401,"请先登录！");
        }
        return new SimpleResponse(200,"已登录",authentication.getPrincipal());
    }

    @GetMapping("/isLogout")
    public SimpleResponse isLogout(){
        return new SimpleResponse(200,"退出当前用户成功！将返回登陆页面！");
    }

    /**
     * 测试错误用例。
     * @return
     */
    @GetMapping("/testErr")
    public SimpleResponse testErr(){
//        int i = 1/0;
        throw  new BusinessException(999,"该用户不存在！");
//        return new SimpleResponse(200,"测试错误！");
    }
}
