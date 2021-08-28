package com.lzz.onlineexam.dao;

import com.lzz.onlineexam.entity.FillQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 填空题题库
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Mapper
public interface FillQuestionDao extends BaseMapper<FillQuestionEntity> {

}
