package cn.xiuminglee.vboot.modules.system.service.impl;

import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.mapper.UserMapper;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiuminglee
 * @since 2018-08-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询用户列表，显示部分字段。
     * @return
     */
    @Override
    public Page<Map> selectAllUserPortion(Page<Map> page) {
        return page.setRecords(userMapper.selectAllUserPortion(page));
    }

    @Override
    public User selectUserInfoNoPass(Integer id) {
        User user = userMapper.selectUserInfoNoPass(id);
        return user;
    }


}
