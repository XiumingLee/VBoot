package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.modules.system.entity.MenuRole;
import cn.xiuminglee.vboot.modules.system.entity.Role;
import cn.xiuminglee.vboot.modules.system.service.MenuRoleService;
import cn.xiuminglee.vboot.modules.system.service.RoleService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 22
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuRoleService menuRoleService;

    /**
     * 获取全部角色信息展示在角色管理界面。
     * 或用于模糊查询
     * @return
     */
    @GetMapping("roles")
    public SimpleResponse getRoles(@RequestParam(required = false) Integer page, @RequestParam(required = false) String selectField){

        //如果都为空则查询所有角色。
        if ( (page == null) && StringUtils.isEmpty(selectField)){
            List<Role> roles = roleService.selectList(new EntityWrapper<Role>());
            return new SimpleResponse(200,"查询全部角色成功！",roles);
        } else if(page != null && StringUtils.isEmpty(selectField)){
            Page<Role> rolePage = roleService.selectPage(new Page<Role>(page, 10), new EntityWrapper<Role>());
            return new SimpleResponse(200,"",rolePage);
        }
        Page<Role> rolePage = roleService.selectPage(new Page<Role>(page, 10), new EntityWrapper<Role>().like("name",selectField).or().like("nameZh",selectField));
        return new SimpleResponse(200,"",rolePage);
    }

    @PostMapping("")
    public SimpleResponse addRole(@RequestBody Role role){
        logger.info(String.valueOf(role));
        boolean b = roleService.insert(role);
        if (b){
            return  new SimpleResponse(200,"添加角色成功！");
        }
        return new SimpleResponse(999,"添加角色失败！");
    }

    /**
     * 修改角色信息
     * @param role
     * @return
     */
    @PutMapping("")
    public SimpleResponse updateRole(@RequestBody Role role){
        logger.info(String.valueOf(role));
        boolean b = roleService.updateById(role);
        if (b){
            return  new SimpleResponse(200,"修改角色成功！");
        }
        return new SimpleResponse(999,"修改角色失败！");
    }

    /**
     * 根据角色id，获取权限信息。
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public SimpleResponse getAuthById(@PathVariable Integer id){
        logger.info(String.valueOf(id));
        List<Integer> mids = menuRoleService.selectMidsByRid(id);
        return new SimpleResponse(200,"",mids);
    }

    /**
     * 修改角色权限，并清空url缓存
     * @param map
     * @return
     */
    @PostMapping("/menuRole")
    @CacheEvict(cacheNames = "url",allEntries=true)
    public SimpleResponse setMenuRole(@RequestBody Map map){
        Integer rid = (Integer) map.get("rid");
        //先将rid的全部删除
        boolean b = menuRoleService.delete(new EntityWrapper<MenuRole>().eq("rid", rid));
        List<Integer> ids = (List<Integer>) map.get("ids");
        if (b){
            List<MenuRole> menuRoleList = new ArrayList<MenuRole>();
            ids.forEach(integer -> {
                MenuRole menuRole = new MenuRole();
                menuRole.setMid(integer);
                menuRole.setRid(rid);
                menuRoleList.add(menuRole);
            });
            //批量插入
            boolean b1 = menuRoleService.insertBatch(menuRoleList);
            if (b1){
                return new SimpleResponse(200,"修改权限完成！");
            }
        }
        return new SimpleResponse(999,"修改权限失败！");
    }

}
