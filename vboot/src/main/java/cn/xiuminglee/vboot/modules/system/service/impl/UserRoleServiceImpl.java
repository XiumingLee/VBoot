package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.UserRole;
import cn.xiuminglee.vboot.modules.system.mapper.UserRoleMapper;
import cn.xiuminglee.vboot.modules.system.service.UserRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public String getRoleByUserId(Integer id) {
        return userRoleMapper.getRoleByUserId(id);
    }
}
