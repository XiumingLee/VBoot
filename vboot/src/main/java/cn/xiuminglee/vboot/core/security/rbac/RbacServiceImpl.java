package cn.xiuminglee.vboot.core.security.rbac;

import cn.xiuminglee.vboot.core.security.authentication.MyUserDetails;
import cn.xiuminglee.vboot.modules.system.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component("rbacService")
public class RbacServiceImpl  implements RbacService{
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private MenuService menuService;
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        boolean hasPermission = false;
        Object principal = authentication.getPrincipal();
        //判断是否是匿名用户
        if (principal instanceof UserDetails){
            //获取当去用户id
            Integer userId = ((MyUserDetails) principal).getId();
//            String method = request.getMethod();
//            System.out.println(method);
            //读取用户所拥有的所有url
            List<RBACEntity> urls = menuService.findUserUrlById(userId);

            for (RBACEntity url : urls){
                //判断urls中有没有和当前请求的url匹配的。
                if (antPathMatcher.match(url.getUrl(),request.getRequestURI()) && StringUtils.equals(url.getMethod(),request.getMethod())){
                    hasPermission=true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
