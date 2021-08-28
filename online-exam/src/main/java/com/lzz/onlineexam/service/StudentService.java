package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.ScoreEntity;
import com.lzz.onlineexam.entity.StudentEntity;

import java.util.Map;

/**
 * 学生信息表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface StudentService extends IService<StudentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //注册
    R register(StudentEntity studentEntity);

    //个人信息
    String studentInfo(String token , String studentName);

    //我的试卷
    Page<ScoreEntity> myExamRecord(String studentId , Integer page , Integer size);

    IPage<StudentEntity> studentsInfo(Integer page, Integer size);
}

