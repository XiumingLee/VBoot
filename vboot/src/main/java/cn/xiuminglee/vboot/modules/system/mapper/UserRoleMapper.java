package cn.xiuminglee.vboot.modules.system.mapper;

import cn.xiuminglee.vboot.modules.system.entity.UserRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-09
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    String getRoleByUserId(Integer id);

}
