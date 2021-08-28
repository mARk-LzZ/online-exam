package com.lzz.onlineexam.dao;

import com.lzz.onlineexam.entity.ScoreEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 成绩管理表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Mapper
public interface ScoreDao extends BaseMapper<ScoreEntity> {
	
}
