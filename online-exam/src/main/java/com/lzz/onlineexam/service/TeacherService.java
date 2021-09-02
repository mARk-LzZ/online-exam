package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.entity.TeacherEntity;

import java.util.List;
import java.util.Map;

/**
 * 教师信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface TeacherService extends IService<TeacherEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /*
    * 教师注册
    * */
    R register(TeacherEntity teacherEntity);

    //个人信息(管理员)
    String teacherInfo(String token , String teacherName);

    //查看所有学生
    IPage<StudentEntity> studentsInfo(Integer page , Integer size);

    //所有教师信息
    IPage<TeacherEntity> teachersInfo(Integer page, Integer size);

    //登陆后查看自己信息
    String myInfo(String token , String teacherName);
}

