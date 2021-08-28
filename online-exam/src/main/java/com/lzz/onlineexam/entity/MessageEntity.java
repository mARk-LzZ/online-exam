package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 留言表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("message")
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 留言编号
	 */
	@TableId(type=IdType.ASSIGN_ID)
	private String id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 留言内容
	 */
	private String content;
	/**
	 * 留言时间
	 */
	private Date time;

}
