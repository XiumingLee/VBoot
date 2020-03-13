package cn.xiuminglee.vboot.modules.system.service;

import cn.xiuminglee.vboot.modules.system.entity.User;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-30
 */
public interface UserService extends IService<User> {
    Page<Map> selectAllUserPortion(Page<Map> page);
    User selectUserInfoNoPass(Integer id);
}
