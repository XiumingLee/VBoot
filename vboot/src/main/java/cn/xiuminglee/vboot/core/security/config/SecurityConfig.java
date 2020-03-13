package cn.xiuminglee.vboot.core.security.config;

import cn.xiuminglee.vboot.core.security.authentication.MyAuthenticationFailureHandler;
import cn.xiuminglee.vboot.core.security.authentication.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author Xiuming Lee
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/loginMust")
                .loginProcessingUrl("/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler);
        http.authorizeRequests()
                //放行PreFlightRequest的请求
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/login", "/loginMust","/isLogin","/isLogout","/system/userUrls").permitAll()
                .antMatchers("/system/user/me/*","/system/mail/me/codegenerate","/chat/**").authenticated()
                .antMatchers(HttpMethod.GET, "/system/menu","/system/role/*","/system/dept/deptsTree","/system/dict/dict/*").authenticated()
                .antMatchers("/system/code/**").hasRole("ADMIN")
                .anyRequest()
                .access("@rbacService.hasPermission(request,authentication)");

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/isLogout");
        http.csrf().disable();
        http.cors();
    }



    /**
     * 密码加盐加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder (){
        return  new BCryptPasswordEncoder();
    }

}
