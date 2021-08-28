package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.lzz.onlineexam.dao.SubjectiveQuestionDao;
import com.lzz.onlineexam.entity.SubjectiveQuestionEntity;
import com.lzz.onlineexam.service.SubjectiveQuestionService;


@Service("subjectiveQuestionService")
public class SubjectiveQuestionServiceImpl extends ServiceImpl<SubjectiveQuestionDao, SubjectiveQuestionEntity> implements SubjectiveQuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SubjectiveQuestionEntity> page = this.page(
                new Query<SubjectiveQuestionEntity>().getPage(params),
                new QueryWrapper<SubjectiveQuestionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<SubjectiveQuestionEntity> subjectiveQuestionsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }

}