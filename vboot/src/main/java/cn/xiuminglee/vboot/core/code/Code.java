package cn.xiuminglee.vboot.core.code;

import java.time.LocalDateTime;

/**
 * @Author Xiuming Lee
 * 验证码实体,
 * 包含验证码和过期时间
 */
public class Code {

    private String code;
    private LocalDateTime expireTime;


    /**
     * @param code  验证码
     * @param expireIn 多长时间过期 单位s
     */
    public Code(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * @param code
     * @param expireTime 过期的时间
     */
    public Code(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     * @return boolen 判断是否过期
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
