package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.TreeBuilder;
import cn.xiuminglee.vboot.core.security.authentication.MyUserDetails;
import cn.xiuminglee.vboot.core.security.rbac.RBACEntity;
import cn.xiuminglee.vboot.modules.system.entity.Menu;
import cn.xiuminglee.vboot.modules.system.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Xiuming Lee
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public SimpleResponse menuNode(){
        //获取当前登录的用户id
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menuByUserId = menuService.findMenuByUserId(userDetails.getId());
        String buildTree = new TreeBuilder<Menu>().buildTree(menuByUserId);
        return new SimpleResponse(200,"请求菜单列表成功！",buildTree);
    }

    @GetMapping("/userUrls")
    public SimpleResponse userUrls(){
        //获取当前登录的用户id
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<RBACEntity> userUrls = menuService.findUserUrlById(userDetails.getId());
        return new SimpleResponse(200,"",userUrls);
    }
}
