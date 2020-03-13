package cn.xiuminglee.vboot.core.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author 22
 * 验证码验证器
 */
public interface ValidateCode {

    /**
     *
     * @param webRequest
     * @param code   验证码
     * @param number  邮箱或手机号
     * @return
     */
    boolean validate(ServletWebRequest webRequest,String code,String number);
}
