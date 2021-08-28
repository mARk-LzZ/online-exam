package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.ReplayDao;
import com.lzz.onlineexam.entity.ReplayEntity;
import com.lzz.onlineexam.service.ReplayService;


@Service("replayService")
public class ReplayServiceImpl extends ServiceImpl<ReplayDao, ReplayEntity> implements ReplayService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ReplayEntity> page = this.page(
                new Query<ReplayEntity>().getPage(params),
                new QueryWrapper<ReplayEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IPage<ReplayEntity> replaysInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }

}