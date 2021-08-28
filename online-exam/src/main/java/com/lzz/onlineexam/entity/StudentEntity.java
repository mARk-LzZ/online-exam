package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;
import org.omg.CORBA.IDLType;

/**
 * 学生信息表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@TableName("student")
public class StudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type=IdType.ASSIGN_ID)
	private Long studentid;
	/**
	 * 姓名
	 */
	private String studentname;
	/**
	 * 年级
	 */
	private String grade;
	/**
	 * 专业
	 */
	private String major;
	/**
	 * 班级
	 */
	private String clazz;
	/**
	 * 学院
	 */
	private String institute;
	/**
	 * 电话号码
	 */
	private String tel;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 账号
	 */
	private String cardid;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 角色(0管理员，1教师，2学生)
	 */
	private Integer role;

	/*
	 * 验证码
	 * */
	@TableField(exist=false , select= false)
	private String vercode;

}
