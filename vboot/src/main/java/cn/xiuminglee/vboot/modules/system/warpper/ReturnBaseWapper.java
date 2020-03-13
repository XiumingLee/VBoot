package cn.xiuminglee.vboot.modules.system.warpper;

import java.util.List;
import java.util.Map;

/**
 * 查询结果返回的包装类基类
 * @author xiuminglee
 */
public abstract class ReturnBaseWapper {
    public Object obj = null;

    /**
     * 传入的Object必须是List<Map<String, Object>>，或者Map<String, Object>
     * @param obj
     */
    public ReturnBaseWapper(Object obj) {
        this.obj = obj;
    }

    public Object warp() {
        if (this.obj instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) this.obj;
            for (Map<String, Object> map : list) {
                warpTheMap(map);
            }
            return list;
        } else if (this.obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) this.obj;
            warpTheMap(map);
            return map;
        } else {
            return this.obj;
        }
    }

    /**
     * 子类去实现要加工的字段
     * @param map
     */
    protected abstract void warpTheMap(Map<String, Object> map);
}
