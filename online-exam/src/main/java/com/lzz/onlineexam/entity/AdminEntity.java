package com.lzz.onlineexam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 管理员信息表
 * 
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@TableName("admin")
public class AdminEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID号
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String adminid;
	/**
	 * 姓名
	 */
	private String adminname;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 电话号码
	 */
	private String tel;
	/**
	 * 电子邮箱
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
	 * 角色(0管理员，1教师，2学生)
	 */
	private String role;

}
