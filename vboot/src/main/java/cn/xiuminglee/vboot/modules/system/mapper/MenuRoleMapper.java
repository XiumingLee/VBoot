package cn.xiuminglee.vboot.modules.system.mapper;

import cn.xiuminglee.vboot.modules.system.entity.MenuRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiuminglee
 * @since 2018-09-08
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    List<Integer> selectMidsByRid(Integer id);
}
