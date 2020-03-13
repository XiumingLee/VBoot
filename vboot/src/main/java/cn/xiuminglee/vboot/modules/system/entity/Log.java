package cn.xiuminglee.vboot.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 错误日志表
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-28
 */
@TableName("vb_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 异常名称
     */
    private String name;
    /**
     * 错误状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 错误的详细信息
     */
    private String details;
    /**
     * 其他信息
     */
    private String other;
    /**
     * 创建的时间
     */
    private Date createdata;

    private Log() {
    }
    /**
     *
     * @param name  错误日志名称  “业务错误日志”、“自定义抛出错误”、“登录异常”
     * @param code  错误状态码
     * @param msg   错误提示信息
     * @param details  错误详细信息
     * @param other    错误的其他信息
     */
    public Log( String name, Integer code, String msg, String details, String other) {
        this.name = name;
        this.code = code;
        this.msg = msg;
        this.details = details;
        this.other = other;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Date getCreatedata() {
        return createdata;
    }

    public void setCreatedata(Date createdata) {
        this.createdata = createdata;
    }

    @Override
    public String toString() {
        return "Log{" +
        ", id=" + id +
        ", name=" + name +
        ", codegenerate=" + code +
        ", msg=" + msg +
        ", details=" + details +
        ", other=" + other +
        ", createdata=" + createdata +
        "}";
    }
}
