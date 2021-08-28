package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 判断题题库表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 19:25:11
 */
@Data
@TableName("subjective_question")
public class SubjectiveQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试题编号
	 */
	@TableId(type=IdType.AUTO)
	private String questionid;
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
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
