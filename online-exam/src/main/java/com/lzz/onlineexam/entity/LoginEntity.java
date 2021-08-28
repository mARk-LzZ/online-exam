package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-25 16:30:03
 */
@Data
@TableName("login")
public class LoginEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 登录id
	 */
	@TableId(type=IdType.ASSIGN_UUID)
	private String loginid;
	/**
	 * 用户账号
	 */
	private String userid;
	/**
	 * 用户权限
	 */
	private Integer role;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 电话
	 */
	private String tel;

	/*
	 * 验证码
	 * */
	@TableField(exist=false , select= false)
	private String vercode;





}
