package com.lzz.onlineexam.dao;

import com.lzz.onlineexam.entity.PaperManageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试卷管理表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Mapper
public interface PaperManageDao extends BaseMapper<PaperManageEntity> {
	
}
