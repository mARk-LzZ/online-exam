package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.ScoreDao;
import com.lzz.onlineexam.entity.ScoreEntity;
import com.lzz.onlineexam.service.ScoreService;


@Service("scoreService")
public class ScoreServiceImpl extends ServiceImpl<ScoreDao, ScoreEntity> implements ScoreService {

    @Autowired
    private StudentAnswerServiceImpl studentAnswerServiceImpl;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ScoreEntity> page = this.page(
                new Query<ScoreEntity>().getPage(params),
                new QueryWrapper<ScoreEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<ScoreEntity> infoByExamCode(Double examCode, Integer page, Integer size) {
        return this.page(new Page<>(page , size) , new QueryWrapper<ScoreEntity>().eq("examCode" , examCode));
    }

    @Override
    public IPage<ScoreEntity> scoresInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}