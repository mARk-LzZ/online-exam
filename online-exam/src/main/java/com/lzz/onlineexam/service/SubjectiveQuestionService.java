package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.entity.SubjectiveQuestionEntity;

import java.util.Map;

/**
 * 判断题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 19:25:11
 */
public interface SubjectiveQuestionService extends IService<SubjectiveQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    IPage<SubjectiveQuestionEntity> subjectiveQuestionsInfo(Integer page, Integer size);
}

