package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 选择题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("multi_question")
public class MultiQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试题编号
	 */
	@TableId(type=IdType.AUTO)
	private double questionid;
	/**
	 * 考试科目
	 */
	private String subject;
	/**
	 * 问题题目
	 */
	private String question;
	/**
	 * 选项A
	 */
	private String answera;
	/**
	 * 选项B
	 */
	private String answerb;
	/**
	 * 选项C
	 */
	private String answerc;
	/**
	 * 选项D
	 */
	private String answerd;
	/**
	 * 正确答案
	 */
	private String rightanswer;
	/**
	 * 题目解析
	 */
	private String analysis;
	/**
	 * 分数
	 */
	private Double score;
	/**
	 * 所属章节
	 */
	private String section;
	/**
	 * 难度等级
	 */
	private String level;

}
