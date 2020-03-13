package cn.xiuminglee.vboot.core.security.authentication;

import cn.xiuminglee.vboot.modules.system.entity.User;
import cn.xiuminglee.vboot.modules.system.service.UserRoleService;
import cn.xiuminglee.vboot.modules.system.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 22
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectOne(new EntityWrapper<User>().eq("username",username));
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在！");
        }
        String roleByUserId = userRoleService.getRoleByUserId(user.getId());
        List<GrantedAuthority> userRole = AuthorityUtils.commaSeparatedStringToAuthorityList(roleByUserId);
        return new MyUserDetails(user.getId(),user.getName(),user.getAvatar(),user.getUsername(),user.getPassword(),userRole);
    }
}
