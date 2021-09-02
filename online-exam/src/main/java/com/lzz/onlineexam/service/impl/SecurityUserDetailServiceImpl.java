package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.dao.AdminDao;
import com.lzz.onlineexam.dao.LoginDao;
import com.lzz.onlineexam.dao.StudentDao;
import com.lzz.onlineexam.dao.TeacherDao;
import com.lzz.onlineexam.entity.*;
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
    private StudentDao studentDao;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<AdminEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("adminName" , username);
        AdminEntity adminEntity = adminDao.selectOne(wrapper1);
        if (adminEntity != null){
            List<GrantedAuthority> adminAuths=AuthorityUtils.commaSeparatedStringToAuthorityList("admin,examManage,fillQuestion,judgeQuestion,login,message,multiQuestion,paperManage,replay,score,studentAnswer,student,subjectiveQuestion,teacher");
            return new User(adminEntity.getAdminname() , adminEntity.getPwd(), adminAuths);
        }

        QueryWrapper<StudentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("studentName" , username);
        StudentEntity studentEntity = studentDao.selectOne(wrapper);
        if (studentEntity!=null){
            List<GrantedAuthority> studentAuths=AuthorityUtils.commaSeparatedStringToAuthorityList("student");
            System.out.println(new User(studentEntity.getStudentname() , studentEntity.getPwd(), studentAuths));
            return new User(studentEntity.getStudentname() , studentEntity.getPwd(), studentAuths);
        }

        QueryWrapper<TeacherEntity> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("teacherName" , username);
        TeacherEntity teacherEntity = teacherDao.selectOne(wrapper2);
        if (teacherEntity!=null){
            List<GrantedAuthority> teacherAuths=AuthorityUtils.commaSeparatedStringToAuthorityList("examManage,fillQuestion,judgeQuestion,login,message,multiQuestion,paperManage,replay,score,studentAnswer,student,subjectiveQuestion,teacher");
            return new User(teacherEntity.getTeachername() , teacherEntity.getPwd() , teacherAuths);
        }

            throw new UsernameNotFoundException("无此用户");



    }
}
