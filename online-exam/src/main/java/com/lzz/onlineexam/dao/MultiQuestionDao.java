package com.lzz.onlineexam.dao;

import com.lzz.onlineexam.entity.FillQuestionEntity;
import com.lzz.onlineexam.entity.MultiQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 选择题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Mapper
public interface MultiQuestionDao extends BaseMapper<MultiQuestionEntity> {

}
