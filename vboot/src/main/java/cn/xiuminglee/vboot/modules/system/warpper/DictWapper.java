package cn.xiuminglee.vboot.modules.system.warpper;

import cn.xiuminglee.vboot.core.common.utils.SpringContextHolder;
import cn.xiuminglee.vboot.modules.system.entity.Dict;
import cn.xiuminglee.vboot.modules.system.service.DictService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.util.List;
import java.util.Map;

/**
 * 字典查询结果返回包装
 * @Author Xiuming Lee
 */
public class DictWapper extends ReturnBaseWapper {

    /**
     * 从Spring上下文获取
     * SpringContextHolder自己写的获取Spring上下文的工具类
     */
    private DictService dictService = SpringContextHolder.getBean(DictService.class);

    public DictWapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        StringBuffer detail = new StringBuffer();
        Integer id = (Integer) map.get("id");
        //查找父级id是该ID的子数据
        List<Dict> dicts = dictService.selectList(new EntityWrapper<Dict>().eq("parentid", id));
        //将其循环包装
        if (dicts != null){
            dicts.forEach(dict -> {
                detail.append(dict.getNum() + ":" +dict.getName() + ";");
            });
            //put到map中
            map.put("detail",detail.toString());
        }

    }
}
