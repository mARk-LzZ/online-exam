package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 考试管理表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("exam_manage")
public class ExamManageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考试编号
	 */
	@TableId
	private Double examcode;
	/**
	 * 该次考试介绍
	 */
	private String description;
	/**
	 * 课程名称
	 */
	private String source;
	/**
	 * 试卷编号
	 */

	private Double paperid;
	/**
	 * 考试日期
	 */
	private String examdate;
	/**
	 * 持续时长
	 */
	private Integer totaltime;
	/**
	 * 年级
	 */
	private String grade;
	/**
	 * 学期
	 */
	private String term;
	/**
	 * 专业
	 */
	private String major;
	/**
	 * 学院
	 */
	private String institute;
	/**
	 * 总分
	 */
	private Integer totalscore;
	/**
	 * 考试类型
	 */
	private String type;
	/**
	 * 考生须知
	 */
	private String tips;

}
