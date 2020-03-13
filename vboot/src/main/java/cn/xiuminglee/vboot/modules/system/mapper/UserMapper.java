package cn.xiuminglee.vboot.modules.system.mapper;

import cn.xiuminglee.vboot.modules.system.entity.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-30
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 分页查询所有用户信息
     * @param page
     * @param state
     * @return
     */
    List<Map> selectAllUserPortion(Page<Map> page);
    User selectUserInfoNoPass(Integer id);
}
