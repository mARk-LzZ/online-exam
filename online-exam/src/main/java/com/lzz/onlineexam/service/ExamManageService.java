package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.entity.ExamManageEntity;

import java.util.Map;

/**
 * 考试管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface ExamManageService extends IService<ExamManageEntity> {

    IPage<ExamManageEntity> examsInfo(Integer page);


}

