package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 试卷管理表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("paper_manage")
public class PaperManageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试卷编号
	 */
	@TableId
	private double paperid;
	/**
	 * 题目类型
	 */
	private Integer questiontype;
	/**
	 * 题目编号
	 */
	private Double questionid;

}
