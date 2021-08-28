package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.AdminEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.entity.TeacherEntity;

import java.util.Map;

/**
 * 管理员信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface AdminService extends IService<AdminEntity> {

    //把数据库数据转移到es上
    PageUtils queryPage(Map<String, Object> params);
    //登录
    R login(AdminEntity adminEntity);
    @Override
    //管理员注册
    boolean save(AdminEntity adminEntity);
    //管理员信息
    String adminInfo(String token ,String adminName);
    //查看所有教师信息
    IPage<TeacherEntity> teacherInfo(Integer page , Integer size);
    //查看所有学生信息
    IPage<StudentEntity> studentInfo(Integer page , Integer size);
    //出卷
    //

    TeacherEntity teacherInfoByname(String teacherName ,Integer page , Integer size);

    IPage<AdminEntity> adminsInfo(Integer page, Integer size);
}

