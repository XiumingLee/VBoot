package cn.xiuminglee.vboot.modules.system.service;

import cn.xiuminglee.vboot.modules.system.entity.MenuRole;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-08
 */
public interface MenuRoleService extends IService<MenuRole> {

    List<Integer> selectMidsByRid(Integer id);
}
