package cn.xiuminglee.vboot.core.code;

import cn.xiuminglee.vboot.modules.system.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @author 22
 * 邮件验证码发送器
 */
@Component("mailCodeSender")
public class MailCodeSender implements CodeSender {

    /**
     * 用于解析thymeleaf模板
     */
    @Resource
    private TemplateEngine templateEngine;

    @Autowired
    private MailService mailService;

    @Override
    public void send(String target, String code){
        Context context = new Context();
        context.setVariable("title","VBoot邮箱绑定");
        context.setVariable("userName","用户名");
        context.setVariable("type","VBoot后台管理系统");
        context.setVariable("captcha",code);
        String mailCodeTemplate = templateEngine.process("mailCodeTemplate", context);
        mailService.htmlMailSend(mailCodeTemplate,target);
    }
}
