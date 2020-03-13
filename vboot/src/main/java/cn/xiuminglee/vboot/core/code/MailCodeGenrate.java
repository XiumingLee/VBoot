package cn.xiuminglee.vboot.core.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author 22
 * 邮箱验证码生成器
 */
@Component("mailCodeGenrate")
public class MailCodeGenrate implements CodeGenerate {
    /**
     *
     * @param webRequest
     * @param number   邮箱号作为session的id
     * @return
     */
    @Override
    public Code generator(ServletWebRequest webRequest,String number) {
        // 生成6位验证码
        String code = RandomStringUtils.randomNumeric(6);
        //10分钟过期
        Code mailCode = new Code(code,600);
        //保存到session中
        webRequest.getRequest().getSession().setAttribute(number,mailCode);
        return mailCode;
    }
}
