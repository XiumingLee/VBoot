package cn.xiuminglee.vboot.core.common.controller;

import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @Author Xiuming Lee
 */
@RestController
public class CommonController {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @GetMapping("/loginMust")
    public SimpleResponse loginMust(){
        return new SimpleResponse(401,"请先登录！");
    }

    /**
     * 供前端请求验证是否登录。
     * @return
     */
    @GetMapping("/isLogin")
    public SimpleResponse isLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

}
