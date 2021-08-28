package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 教师信息表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("teacher")
public class TeacherEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type=IdType.ASSIGN_ID)
	private Long teacherid;
	/**
	 * 姓名
	 */
	private String teachername;
	/**
	 * 学院
	 */
	private String institute;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 电话号码
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 身份证号
	 */
	private String cardid;
	/**
	 * 职称
	 */
	private String type;
	/**
	 * 角色（0管理员，1教师，2学生）
	 */
	private Integer role;

	/*
	 * 验证码
	 * */
	@TableField(exist=false , select= false)
	private String vercode;

}
