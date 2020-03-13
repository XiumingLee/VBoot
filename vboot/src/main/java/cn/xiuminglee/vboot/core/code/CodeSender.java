package cn.xiuminglee.vboot.core.code;

/**
 * @author 22
 * 验证码发送者
 */
public interface CodeSender {
    /**
     * @param target 发送的目标或目的地,比如接受邮件的邮件名,手机号等
     * @param code  验证码信息
     */
    void send(String target,String code);
}
