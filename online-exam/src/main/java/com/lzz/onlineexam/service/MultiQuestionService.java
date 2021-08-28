package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import com.lzz.onlineexam.entity.MultiQuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * 选择题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface MultiQuestionService extends IService<MultiQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);


    IPage<MultiQuestionEntity> multiQuestionsInfo(Integer page, Integer size);
}

