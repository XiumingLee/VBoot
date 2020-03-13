package cn.xiuminglee.vboot.core.common.utils;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * 继承自Mybatis PLus的page类，用于特殊数据的返回使用。
 * @author 22
 */
public class PageProcessor<T> extends Page<T> {
    /**
     * 查询数据列表
     */
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
