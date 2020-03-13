package cn.xiuminglee.vboot.modules.system.warpper;

import cn.xiuminglee.vboot.core.common.utils.SpringContextHolder;
import cn.xiuminglee.vboot.modules.system.entity.Dict;
import cn.xiuminglee.vboot.modules.system.service.DictService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import java.util.Map;

public class MenuWapper extends ReturnBaseWapper {

    private DictService dictService = SpringContextHolder.getBean(DictService.class);

    public MenuWapper(Object obj) {
        super(obj);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        StringBuffer ismenu = new StringBuffer();
        StringBuffer status = new StringBuffer();
        Dict dictStatus = dictService.selectOne(new EntityWrapper<Dict>().eq("parentid", 1).eq("num",(Integer)map.get("enabled")));
        Dict dictIsMenu = dictService.selectOne(new EntityWrapper<Dict>().eq("parentid", 4).eq("num", (Integer)map.get("ismenu")));
        map.put("ismenu_name",ismenu.append(dictIsMenu.getName()).toString());
        map.put("enabled_name",status.append(dictStatus.getName()).toString());

    }
}
