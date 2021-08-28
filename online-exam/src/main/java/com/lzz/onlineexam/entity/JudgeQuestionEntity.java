package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 判断题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("judge_question")
public class JudgeQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试题编号
	 */
	@TableId(type=IdType.AUTO)
	private Double questionid;
	/**
	 * 考试科目
	 */
	private String subject;
	/**
	 * 试题内容
	 */
	private String question;
	/**
	 * 正确答案
	 */
	private String answer;
	/**
	 * 题目解析
	 */
	private String analysis;
	/**
	 * 分数
	 */
	private Double score;
	/**
	 * 难度等级
	 */
	private String level;
	/**
	 * 所属章节
	 */
	private String section;

}
