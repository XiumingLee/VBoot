package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.code.Code;
import cn.xiuminglee.vboot.core.code.MailCodeGenrate;
import cn.xiuminglee.vboot.core.code.MailCodeSender;
import cn.xiuminglee.vboot.core.code.MailValidateCode;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @Author Xiuming Lee
 */
@RestController
@RequestMapping("/system/mail")
public class MailController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MailCodeGenrate mailCodeGenrate;
    @Autowired
    private MailCodeSender mailCodeSender;
    @Autowired
    private MailValidateCode mailValidateCode;
    @Autowired
    private UserService userService;

    /**
     * 发送邮件到邮箱,用于绑定邮箱
     * @param email
     * @param webRequest
     * @return
     */
    @GetMapping("/me/code")
    public SimpleResponse mailCodeGenerate(@RequestParam String email, ServletWebRequest webRequest){
        log.info(email);
        if (StringUtils.isEmpty(email)){
            return new SimpleResponse(999,"请填写邮箱地址");
        }
        Code mailCode = mailCodeGenrate.generator(webRequest,email);
        new Thread(()->{
            mailCodeSender.send(email,mailCode.getCode());
        }).start();
        return new SimpleResponse(200,"请注意查收邮件!");
    }

    @PostMapping("/me/code")
    public SimpleResponse mailCodeValidate(@RequestBody Map mail,ServletWebRequest webRequest){
        String email = (String) mail.get("email");
        String code = (String) mail.get("codegenerate");
        boolean validate = mailValidateCode.validate(webRequest, code, email);
        if (validate){
            //获得当前用户id
            Integer userId = VBootUtils.currentUserInfo().getId();
            User user = new User();
            user.setId(userId);
            user.setMail(email);
            boolean b = userService.updateById(user);
            if (b){
                return new SimpleResponse(200,"绑定邮箱成功!");
            }else {
                return new SimpleResponse(999,"绑定邮箱失败!");
            }
        }else {
            return new SimpleResponse(999,"验证码验证失败!");
        }
    }
}
