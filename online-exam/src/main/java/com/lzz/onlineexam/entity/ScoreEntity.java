package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 成绩管理表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("score")
public class ScoreEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分数编号
	 */
	@TableId(type=IdType.AUTO)
	private Integer scoreid;
	/**
	 * 考试编号
	 */
	private Double examcode;
	/**
	 * 学号
	 */
	private Double studentid;
	/**
	 * 课程名称
	 */
	private String subject;
	/**
	 * 总成绩
	 */
	private Double score;
	/**
	 * 答题日期
	 */
	private String answerdate;

}
