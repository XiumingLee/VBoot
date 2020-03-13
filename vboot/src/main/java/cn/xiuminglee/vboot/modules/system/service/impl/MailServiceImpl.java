package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.core.common.exception.BusinessException;
import cn.xiuminglee.vboot.modules.system.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
/**
 * @author 22
 */
@Service
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String sendUser;
    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Override
    public void htmlMailSend(String context,String target) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper  = new MimeMessageHelper(mimeMessage,true);
            // 邮件设置
            helper.setSubject("VBoot系统邮箱绑定");
            helper.setText(context,true);
            helper.setTo(target);
            helper.setFrom(sendUser);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(999,"邮件发送失败!");
        }

    }
}
