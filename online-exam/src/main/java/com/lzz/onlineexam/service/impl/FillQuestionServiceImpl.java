package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.FillQuestionDao;
import com.lzz.onlineexam.entity.FillQuestionEntity;
import com.lzz.onlineexam.service.FillQuestionService;


@Service("fillQuestionService")
public class FillQuestionServiceImpl extends ServiceImpl<FillQuestionDao, FillQuestionEntity> implements FillQuestionService {

   private FillQuestionDao fillQuestionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FillQuestionEntity> page = this.page(
                new Query<FillQuestionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    @Override
    public IPage<FillQuestionEntity> fillQuestionsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }

}