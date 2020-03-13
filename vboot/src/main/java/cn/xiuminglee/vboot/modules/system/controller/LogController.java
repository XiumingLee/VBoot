package cn.xiuminglee.vboot.modules.system.controller;


import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.modules.system.entity.Log;
import cn.xiuminglee.vboot.modules.system.service.LogService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 错误日志表 前端控制器
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-28
 */
@RestController
@RequestMapping("/system/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    public SimpleResponse getLogsByPage(@RequestParam Integer page){
        Page<Log> logPage = logService.selectPage(new Page<Log>(page, 10), new EntityWrapper<Log>().orderBy("createdata", false));
        return  new SimpleResponse(200,"查询成功!",logPage);
    }

    /**
     * 根据id删除相应的错误日志
     * @param id
     * @return
     */
    @DeleteMapping("/log/{id}")
    public SimpleResponse delLogById(@PathVariable Integer id){
        boolean b = logService.deleteById(id);
        if (b){
            return new SimpleResponse(200,"删除成功!");
        }
        return new SimpleResponse(999,"删除失败!");
    }

    /**
     * 根据ids批量删除错误日志
     * @param idList
     * @return
     */
    @DeleteMapping("/logs")
    public SimpleResponse batchDelLogs(@RequestBody List<Integer> idList){
        boolean b = logService.deleteBatchIds(idList);
        if (b){
            return new SimpleResponse(200,"删除成功!");
        }
        return new SimpleResponse(999,"删除失败!");
    }
}

