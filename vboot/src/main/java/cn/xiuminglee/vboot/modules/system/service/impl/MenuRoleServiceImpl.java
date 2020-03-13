package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.MenuRole;
import cn.xiuminglee.vboot.modules.system.mapper.MenuRoleMapper;
import cn.xiuminglee.vboot.modules.system.service.MenuRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-08
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Resource
    private MenuRoleMapper menuRoleMapper;

    @Override
    public List<Integer> selectMidsByRid(Integer id) {
        return menuRoleMapper.selectMidsByRid(id);
    }
}
