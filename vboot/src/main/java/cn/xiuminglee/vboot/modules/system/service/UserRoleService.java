package cn.xiuminglee.vboot.modules.system.service;

import cn.xiuminglee.vboot.modules.system.entity.UserRole;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-09
 */
public interface UserRoleService extends IService<UserRole> {

    String getRoleByUserId(Integer id);

}
