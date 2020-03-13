package cn.xiuminglee.vboot.core.security.rbac;

/**
 * @Author Xiuming Lee
 * 依赖存放当前用户的可访问的url和请求方法。
 */
public class RBACEntity {
    String url;
    String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "RBACEntity{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
