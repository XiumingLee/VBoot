package cn.xiuminglee.vboot.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 聊天记录表
 * </p>
 *
 * @author xiuminglee
 * @since 2018-10-10
 */
@TableName("vb_chat_msg")
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 发送消息用户的id
     */
    private Integer sendUserId;
    /**
     * 接收消息用户的ID
     */
    private Integer receiveUserId;
    /**
     * 发送的消息
     */
    private String msg;
    /**
     * 是否读取（0：未读；1：已读）
     */
    private Integer signFlag;
    /**
     * 时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Integer receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChatMsg{" +
        ", id=" + id +
        ", sendUserId=" + sendUserId +
        ", receiveUserId=" + receiveUserId +
        ", msg=" + msg +
        ", signFlag=" + signFlag +
        ", createTime=" + createTime +
        "}";
    }
}
