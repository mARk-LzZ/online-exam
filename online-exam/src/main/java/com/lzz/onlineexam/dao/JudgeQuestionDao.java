package com.lzz.onlineexam.dao;

import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 判断题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Mapper
public interface JudgeQuestionDao extends BaseMapper<JudgeQuestionEntity> {

}
