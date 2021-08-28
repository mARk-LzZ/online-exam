package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.MultiQuestionDao;
import com.lzz.onlineexam.entity.MultiQuestionEntity;
import com.lzz.onlineexam.service.MultiQuestionService;


@Service("multiQuestionService")
public class MultiQuestionServiceImpl extends ServiceImpl<MultiQuestionDao, MultiQuestionEntity> implements MultiQuestionService {

    private MultiQuestionDao multiQuestionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MultiQuestionEntity> page = this.page(
                new Query<MultiQuestionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<MultiQuestionEntity> multiQuestionsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}