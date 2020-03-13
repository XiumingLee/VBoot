package cn.xiuminglee.vboot.core.security.rbac;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Xiuming Lee
 * 基于角色的访问控制（Role-Based-Access-Control）
 */
public interface RbacService {
    /**
     *
     * @param request  请求的信息。
     * @param authentication 当前用户的信息。
     * @return
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
