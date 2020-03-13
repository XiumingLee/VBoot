package cn.xiuminglee.vboot.modules.system.mapper;

import cn.xiuminglee.vboot.core.common.entity.MenuOption;
import cn.xiuminglee.vboot.core.security.rbac.RBACEntity;
import cn.xiuminglee.vboot.modules.system.entity.Menu;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-31
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findMenuByUserId(int id);

    /**
     * 查询用于菜单界面选择层级的。
     * @return
     */
    List<MenuOption> findMenuOption();

    /**
     * 根据用户id查询对应的url
     * @param id
     * @return
     */
    List<RBACEntity> findUserUrlById(Integer id);

}
