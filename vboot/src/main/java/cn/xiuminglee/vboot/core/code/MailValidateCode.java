package cn.xiuminglee.vboot.core.code;

import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * @Author Xiuming Lee
 * 验证邮箱验证码是否正确
 */
@Component("mailValidateCode")
public class MailValidateCode implements ValidateCode {
    @Override
    public boolean validate(ServletWebRequest webRequest, String code ,String number) {
        //从session中获取验证码
        Code mailSessionCode = (Code) webRequest.getRequest().getSession().getAttribute(number);
        if (mailSessionCode == null){
            throw new BusinessException(999,"验证码不存在!");
//            return false;
        }
        if (mailSessionCode.isExpried()){
            throw new BusinessException(999,"验证码已过期!");
//            return false;
        }
        if (StringUtils.equals(mailSessionCode.getCode(),code)){
            return true;
        }
        return false;
    }
}
