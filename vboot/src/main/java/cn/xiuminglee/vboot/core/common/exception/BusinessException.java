package cn.xiuminglee.vboot.core.common.exception;

/**
 * @Author Xiuming Lee
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    //自定义错误码
    private Integer code;

    private BusinessException() {
    }

    public BusinessException(Integer code,String message) {
        super(message);
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
