package com.lzz.onlineexam.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzz.onlineexam.entity.SubjectiveQuestionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 判断题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 19:25:11
 */
@Mapper
public interface SubjectiveQuestionDao extends BaseMapper<SubjectiveQuestionEntity> {
	
}
