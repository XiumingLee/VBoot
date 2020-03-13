package cn.xiuminglee.vboot.modules.netty.pojo;

import cn.xiuminglee.vboot.modules.system.entity.ChatMsg;

import java.io.Serializable;

/**
 * @author 22
 */
public class MsgContent implements Serializable {

    private static final long serialVersionUID = -7408341836850082773L;
    /**
     *    动作类型
     */
    private Integer action;
    /**
     * 用户的聊天内容entity
     */
    private ChatMsg chatMsg;
    /**
     * // 扩展字段
     */
    private String extand;

    public Integer getAction() {
        return action;
    }
    public void setAction(Integer action) {
        this.action = action;
    }
    public ChatMsg getChatMsg() {
        return chatMsg;
    }
    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }
    public String getExtand() {
        return extand;
    }
    public void setExtand(String extand) {
        this.extand = extand;
    }

    @Override
    public String toString() {
        return "MsgContent{" +
                "action=" + action +
                ", chatMsg=" + chatMsg +
                ", extand='" + extand + '\'' +
                '}';
    }
}
