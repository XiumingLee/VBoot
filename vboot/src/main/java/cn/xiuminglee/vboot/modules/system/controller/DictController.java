package cn.xiuminglee.vboot.modules.system.controller;

import cn.xiuminglee.vboot.core.common.utils.PageProcessor;
import cn.xiuminglee.vboot.core.common.utils.SimpleResponse;
import cn.xiuminglee.vboot.core.common.utils.VBootUtils;
import cn.xiuminglee.vboot.modules.system.entity.Dict;
import cn.xiuminglee.vboot.modules.system.service.DictService;
import cn.xiuminglee.vboot.modules.system.warpper.DictWapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/dict")
public class DictController {
    private final Logger logger = LoggerFactory.getLogger(DictController.class);
    @Autowired
    private DictService dictService;


    /**
     * 根据id查询相应字典信息
     * @param id
     * @return
     */
    @GetMapping("/dict/{id}")
    public Object getDictById(@PathVariable("id") Integer id){
        List<Dict> dictList = dictService.selectList(new EntityWrapper<Dict>().eq("parentid", id));
        if (dictList == null){
            return new SimpleResponse(404,"未找到该id对应的字典信息！");
        }
        return  new SimpleResponse(200,"成功！",dictList);
    }

    /**
     * 获取所有字典信息并进行加工返回
     * @return
     */
    @GetMapping("/dicts")
    public Object getDicts(@RequestParam Integer page){
        //分页查询
        Page<Dict> dictPage = dictService.selectPage(new Page<Dict>(page, 10), new EntityWrapper<Dict>().eq("parentid", 0));
        //类型转换
        List<Map<String, Object>> mapList = VBootUtils.objectsToMaps(dictPage.getRecords()) ;
         // 调用DictWapper将字典信息重新包装
        Object warp = new DictWapper(mapList).warp();
        //数据再包装
        PageProcessor pageProcessor = new PageProcessor();
        pageProcessor.setData(warp);
        pageProcessor.setTotal(dictPage.getTotal());
        pageProcessor.setCurrent(dictPage.getCurrent());
        return  new SimpleResponse(200,"",pageProcessor);
    }

    /**
     * 添加或修改字典，先先判断是否有id，有就先删除。
     * @param dict
     * @return
     */
    @PostMapping("")
    public SimpleResponse addDict(@RequestBody Dict dict){
        logger.info(String.valueOf(dict));
        //如果传过来的数据id不为空，先删除，再保存。
        Integer dictId = dict.getId();
        if (!StringUtils.isEmpty(dictId)){
            boolean b1 = dictService.deleteById(dictId);
            boolean b2 = dictService.delete(new EntityWrapper<Dict>().eq("parentid", dictId));
            if (!b1 || !b2){
                return new SimpleResponse(999,"修改字典失败");
            }
        }
        //先保存父级元素
        dict.setParentid(0);
        boolean insert = dictService.insert(dict);
        if (insert){
            List<Dict> childDicts = dict.getChildren();
            childDicts.forEach(childDict -> {
                childDict.setParentid(dict.getId());
            });
            boolean insertBatch = dictService.insertBatch(childDicts);
            if (insertBatch){
                return new SimpleResponse(200,"添加字典完成！");
            }else {
                return new SimpleResponse(999,"添加字典失败！");
            }
        }else {
            return new SimpleResponse(999,"添加字典失败！");

        }
    }


    @GetMapping("/name")
    public SimpleResponse getDictByName(@RequestParam  String name){
        List<Map<String, Object>> selectMaps = dictService.selectMaps(new EntityWrapper<Dict>().like("name", name).eq("parentid",0));
        // 调用DictWapper将字典信息重新包装
        Object warp = new DictWapper(selectMaps).warp();
        return new SimpleResponse(200,"",warp);
    }
}
