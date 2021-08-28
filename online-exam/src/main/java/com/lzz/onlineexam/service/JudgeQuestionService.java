package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.entity.JudgeQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 判断题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface JudgeQuestionService extends IService<JudgeQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);


    IPage<JudgeQuestionEntity> judgeQuestionsInfo(Integer page, Integer size);
}

