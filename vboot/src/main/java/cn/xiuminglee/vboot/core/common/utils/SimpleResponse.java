package cn.xiuminglee.vboot.core.common.utils;

public class SimpleResponse {
    private Integer status;
    private String msg;
    private Object data;

    /**
     * 返回状态码和提示消息
     * @param status
     * @param msg
     */
    public SimpleResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 返回状态码  提示消息和数据
     * @param status
     * @param msg
     * @param data
     */
    public SimpleResponse(Integer status,String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
