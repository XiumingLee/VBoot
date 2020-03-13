package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.Role;
import cn.xiuminglee.vboot.modules.system.mapper.RoleMapper;
import cn.xiuminglee.vboot.modules.system.service.RoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-31
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
