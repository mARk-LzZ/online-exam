package com.lzz.onlineexam.common.config;




import com.lzz.onlineexam.common.handler.MyAuthenticationFailureHandler;
import com.lzz.onlineexam.common.handler.MyAuthenticationSuccessHandler;
import com.lzz.onlineexam.common.handler.MyLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.formLogin()
                .usernameParameter("username")
                .passwordParameter("pwd")
                //需和表单提交的地址一致
                .loginProcessingUrl("/login")
                //自定义登陆页面
                .loginPage("/login.html")
                //登录成功处理器 不能和successForwardUrl共存
                .successHandler(new MyAuthenticationSuccessHandler("/#/main"))
                //登录成功处理器 不能和failureForwardUrl共存
                .failureHandler(new MyAuthenticationFailureHandler("/#/error"));
        //授权认证
        httpSecurity.authorizeRequests()
                //除login和error以外都需要登录验证
                .antMatchers("/#/login").permitAll()
                .antMatchers("/#/error").permitAll()
                .antMatchers("/#/register").permitAll()
                .antMatchers("/#/").permitAll()
                .antMatchers("/js/**" , "/css/**" , "/images/**").permitAll()
                .antMatchers("/onlineexam/admin/*").hasAuthority("admin")
                .antMatchers("/onlineexam/teacher/*").hasAnyAuthority("admin,teacher")

                .anyRequest().authenticated();
        //关闭csrf防护 使得用户可以被认证
        httpSecurity.csrf().disable();

        //退出登录后重定向回登录页面
        httpSecurity.logout()
                .logoutSuccessHandler(new MyLogoutHandler("/#/login"));
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
