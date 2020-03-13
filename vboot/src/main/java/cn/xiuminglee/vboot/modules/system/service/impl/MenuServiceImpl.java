package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.core.common.entity.MenuOption;
import cn.xiuminglee.vboot.core.security.rbac.RBACEntity;
import cn.xiuminglee.vboot.modules.system.entity.Menu;
import cn.xiuminglee.vboot.modules.system.mapper.MenuMapper;
import cn.xiuminglee.vboot.modules.system.service.MenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> findMenuByUserId(int id) {
        return menuMapper.findMenuByUserId(id);
    }

    /**
     * 查询用于菜单界面选择层级的。
     * @return
     */
    @Override
    public List<MenuOption> findMenuOption() {
        return menuMapper.findMenuOption();
    }

    /**
     * 根据用户id查询对应的url
     * 并缓存起来
     * @param id
     * @return
     */
    @Override
    @Cacheable(cacheNames = "urls",key = "#id")
    public List<RBACEntity> findUserUrlById(Integer id) {
        return menuMapper.findUserUrlById(id);
    }
}
