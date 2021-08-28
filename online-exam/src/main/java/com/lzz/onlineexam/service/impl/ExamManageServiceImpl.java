package com.lzz.onlineexam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.ExamManageDao;
import com.lzz.onlineexam.entity.ExamManageEntity;
import com.lzz.onlineexam.service.ExamManageService;


@Service("examManageService")
public class ExamManageServiceImpl extends ServiceImpl<ExamManageDao, ExamManageEntity> implements ExamManageService {



    @Override
    public IPage<ExamManageEntity> examsInfo(Integer page) {

        return this.page(new Page<>(page , 6));
        }
    }

