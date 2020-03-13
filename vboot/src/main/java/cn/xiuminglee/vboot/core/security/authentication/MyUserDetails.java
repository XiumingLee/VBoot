package cn.xiuminglee.vboot.core.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author 22
 */
public class MyUserDetails extends User {

    /**
     * //用户id
     */
    private Integer id;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public MyUserDetails(Integer id, String name,String avatar, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public MyUserDetails(Integer id,String name,String username,String avatar, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
}
