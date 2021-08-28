package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.PaperManageEntity;

import java.awt.print.Paper;
import java.util.List;
import java.util.Map;

/**
 * 试卷管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface PaperManageService extends IService<PaperManageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //组装试卷
    R paperManage();

    //查看试卷
    Map<String , List<Object>> paperSelect(Double paperId);

    IPage<PaperManageEntity> papersInfo(Integer page, Integer size);
}

