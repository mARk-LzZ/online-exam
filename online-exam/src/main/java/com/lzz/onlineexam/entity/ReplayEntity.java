package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 回复表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("replay")
public class ReplayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 留言编号
	 */
	private double messageid;
	/**
	 * 回复编号
	 */
	@TableId(type=IdType.AUTO)
	private double replayid;
	/**
	 * 内容
	 */
	private String replay;
	/**
	 * 回复时间
	 */
	private Date replaytime;

}
