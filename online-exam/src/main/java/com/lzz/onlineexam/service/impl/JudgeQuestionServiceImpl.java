package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.dao.FillQuestionDao;
import com.lzz.onlineexam.entity.FillQuestionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.JudgeQuestionDao;
import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import com.lzz.onlineexam.service.JudgeQuestionService;


@Service("judgeQuestionService")
public class JudgeQuestionServiceImpl extends ServiceImpl<JudgeQuestionDao, JudgeQuestionEntity> implements JudgeQuestionService {

    private JudgeQuestionDao judgeQuestionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<JudgeQuestionEntity> page = this.page(
                new Query<JudgeQuestionEntity>().getPage(params),
                new QueryWrapper<JudgeQuestionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<JudgeQuestionEntity> judgeQuestionsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}