package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.dao.AdminDao;
import com.lzz.onlineexam.dao.LoginDao;
import com.lzz.onlineexam.entity.AdminEntity;
import com.lzz.onlineexam.entity.LoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailService")
public class SecurityUserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private LoginDao loginDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<LoginEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username" , username);
        LoginEntity loginEntity = loginDao.selectOne(wrapper);

        QueryWrapper<AdminEntity> wrapper1 = new QueryWrapper<>();
        wrapper.eq("adminName" , username);
        AdminEntity adminEntity = adminDao.selectOne(wrapper1);

        if (adminEntity != null){
            List<GrantedAuthority> auth=AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
            return new User(adminEntity.getAdminname() , adminEntity.getPwd(), auth);
        }else if (loginEntity!=null){
            List<GrantedAuthority> auths=AuthorityUtils.commaSeparatedStringToAuthorityList("admin,teacher,student");
            return new User(loginEntity.getUsername() , loginEntity.getPwd(), auths);
        }

            throw new UsernameNotFoundException("无此用户");



    }
}
