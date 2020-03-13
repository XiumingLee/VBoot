package cn.xiuminglee.vboot.modules.system.controller;


import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.TreeBuilder;
import cn.xiuminglee.vboot.modules.system.entity.Dept;
import cn.xiuminglee.vboot.modules.system.service.DeptService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-24
 */
@RestController
@RequestMapping("/system/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 获取全部的部门并生成树结构
     * @return
     */
    @GetMapping("/deptsTree")
    public SimpleResponse getDeptTree(){
        List<Dept> deptList = deptService.selectList(new EntityWrapper<Dept>());
        String buildTree = new TreeBuilder<Dept>().buildTree(deptList);
        return new SimpleResponse(200,"",buildTree);
    }

    @PutMapping("/dept")
    public SimpleResponse updateDept(@RequestBody Dept dept){
        boolean b = deptService.updateById(dept);
        if (b){
            return new SimpleResponse(200,"修改成功！");
        }else {
            return  new SimpleResponse(999,"修改失败！");
        }
    }
    @PostMapping("/dept")
    public SimpleResponse addDept(@RequestBody Dept dept){
        boolean b = deptService.insert(dept);
        if (b){
            return new SimpleResponse(200,"添加成功！");
        }else {
            return  new SimpleResponse(999,"添加失败！");
        }
    }
    @DeleteMapping("/{id}")
    public SimpleResponse delDept(@PathVariable Integer id){
        if(id == 0){
            return new SimpleResponse(999,"请选择删除的部门！");
        }else {
            //删除当前id菜单，以及此ID的子菜单
            boolean delete = deptService.delete(new EntityWrapper<Dept>().eq("id",id).or().eq("parentid",id));
            if (delete){
                return new SimpleResponse(200,"删除成功！");
            }
            return new SimpleResponse(999,"删除失败！");
        }
    }

}

