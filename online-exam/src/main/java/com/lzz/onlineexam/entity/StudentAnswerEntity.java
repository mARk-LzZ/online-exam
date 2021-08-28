package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-08-12 00:38:24
 */
@Data
@TableName("student_answer")
public class StudentAnswerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type=IdType.AUTO)
	private Integer id;
	/**
	 * 用户id
	 */
	private Double studentid;
	/**
	 * 班级id
	 */
	private Double classesid;
	/**
	 * 试卷id
	 */
	private Double paperid;

	/*
	* 题目类型
	* */
	private Integer questionType;
	/**
	 * 题目id
	 */
	private Double questionid;
	/**
	 * 用户答案
	 */
	private String userAnswer;

	/*
	* 正确答案
	* */
	private String rightAnswer;
	/**
	 * 用户得分
	 */
	private Double userScore;
	/**
	 * 0:待批改 1:批改完成
	 */
	private Integer questionStatus;

}
