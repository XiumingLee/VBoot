package cn.xiuminglee.vboot.modules.system.service;

/**
 * @Author Xiuming Lee
 * 邮箱服务
 */
public interface MailService {
    void htmlMailSend(String context,String target);
}
