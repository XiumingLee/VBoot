package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.common.entity.MenuOption;
import cn.xiuminglee.vboot.core.common.utils.PageProcessor;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.TreeBuilder;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.Menu;
import cn.xiuminglee.vboot.modules.system.service.MenuService;
import cn.xiuminglee.vboot.modules.system.warpper.MenuWapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    private final Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;

    /**
     * 查询所有菜单，用于菜单管理界面的显示。
     * @return
     * 分页查询
     */
    @GetMapping("/menuList")
    public SimpleResponse menuList(@RequestParam Integer page){
//        logger.info(String.valueOf(page));
        //分页查询
        Page<Menu> menuPage = menuService.selectPage(new Page<Menu>(page, 10), new EntityWrapper<Menu>().ne("id", 0));
        //类型转换
        List<Map<String, Object>> mapList = VBootUtils.objectsToMaps(menuPage.getRecords()) ;
        //字段加工
        Object warp = new MenuWapper(mapList).warp();
        //返回数据包装
        PageProcessor pageProcessor = new PageProcessor();
        pageProcessor.setData(warp);
        pageProcessor.setTotal(menuPage.getTotal());
        pageProcessor.setCurrent(menuPage.getCurrent());
        return  new SimpleResponse(200,"获取所有菜单成功",pageProcessor);
    }

    /**
     * 获取所有菜单，用于菜单层级
     * @return
     */
    @GetMapping("/menuOptions")
    public SimpleResponse menuOptions(){
        List<MenuOption> menuOption = menuService.findMenuOption();
        String buildTree = new TreeBuilder<MenuOption>().buildTree(menuOption);
        return new SimpleResponse(200,"",buildTree);
    }

    /**
     * 增加菜单，路由。
     * @return
     */
    @PostMapping("")
    public SimpleResponse addMenu(@RequestBody Menu menu){
        logger.info(String.valueOf(menu));
        boolean insert = menuService.insert(menu);
        if (insert){
            return new SimpleResponse(200,"添加成功！");
        }
        return new SimpleResponse(999,"添加菜单失败！");
    }
    /**
     * 修改菜单，路由。
     * @return
     */
    @PutMapping("")
    public SimpleResponse editMenu(@RequestBody Menu menu){
        logger.info(String.valueOf(menu));
        boolean update = menuService.updateById(menu);
        if (update){
            return new SimpleResponse(200,"修改成功！");
        }
        return new SimpleResponse(999,"修改菜单失败！");
    }
    /**
     * 根据id查询相应的菜单，路由。
     * @return
     */
    @GetMapping("/{id}")
    public SimpleResponse GetMenu(@PathVariable Integer id){
        logger.info(String.valueOf(id));
        Menu menu = menuService.selectById(id);
        if (menu != null){
            return new SimpleResponse(200,"查询成功！",menu);
        }
        return new SimpleResponse(999,"查询失败！");
    }
    /**
     * 删除菜单，路由。
     * @return
     */
    @DeleteMapping("/{id}")
    public SimpleResponse delMenu(@PathVariable Integer id){
        logger.info(String.valueOf(id));
        if(id == 0){
            return new SimpleResponse(999,"顶级菜单不能删除");
        }else {
            //删除当前id菜单，以及此ID的子菜单
            boolean delete = menuService.delete(new EntityWrapper<Menu>().eq("id",id).or().eq("parentid",id));
            if (delete){
                return new SimpleResponse(200,"删除成功！");
            }
            return new SimpleResponse(999,"删除菜单失败！");
        }
    }
    /**
     * 批量删除删除菜单，路由。
     * @return
     */
    @DeleteMapping("/menus")
    public SimpleResponse delMenus(@RequestBody List<Integer> idsList){
        logger.info(String.valueOf(idsList));
        boolean batchIds = menuService.deleteBatchIds(idsList);
        if (batchIds){
            return new SimpleResponse(200,"删除成功！");
        }
        return new SimpleResponse(999,"删除菜单失败！");
    }

}
