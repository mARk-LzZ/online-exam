package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.ScoreEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 成绩管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface ScoreService extends IService<ScoreEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //根据考试id查询
    IPage<ScoreEntity> infoByExamCode(Double examCode , Integer page , Integer size);

    IPage<ScoreEntity> scoresInfo(Integer page, Integer size);
}

