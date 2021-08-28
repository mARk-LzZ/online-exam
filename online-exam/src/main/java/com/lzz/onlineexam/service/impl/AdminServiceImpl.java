package com.lzz.onlineexam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.Constant;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.dao.StudentDao;
import com.lzz.onlineexam.dao.TeacherDao;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.entity.TeacherEntity;
import com.lzz.onlineexam.service.StudentService;
import com.lzz.onlineexam.service.TeacherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.AdminDao;
import com.lzz.onlineexam.entity.AdminEntity;
import com.lzz.onlineexam.service.AdminService;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;


@Service("adminService")
public class AdminServiceImpl extends ServiceImpl<AdminDao, AdminEntity> implements AdminService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AdminEntity> page = this.page(
                new Query<AdminEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    //登录认证的接口实现
    @Override
    public R login(AdminEntity adminEntity){
        AdminEntity adminEntity1 = this.getOne(new QueryWrapper<AdminEntity>()
                .eq("adminName", adminEntity.getAdminname()));
        if (adminEntity1 != null){
            boolean matches = new BCryptPasswordEncoder().matches(adminEntity.getPwd() , adminEntity1.getPwd());
            if (matches){
                //如果验证通过表明登陆成功

                //生成一个token
                String  token =DigestUtils.md5DigestAsHex(adminEntity.getAdminname().getBytes());

                adminEntity1.setPwd(null);
                //把用户身份储存在redis中
                    redisTemplate.opsForValue().set(Constant.REDIS_ADMIN_LOGIN_KEY + token , JSON.toJSONString(adminEntity1));
                //过期时间
                    redisTemplate.expire(Constant.REDIS_ADMIN_LOGIN_KEY + token , 3 , TimeUnit.HOURS);

                    return R.ok();
            }
        }
        return R.error();
    }

    @Override
    public boolean save(AdminEntity adminEntity) {
        String pwd = new BCryptPasswordEncoder().encode(adminEntity.getPwd());
        adminEntity.setPwd(pwd);
        this.getBaseMapper().insert(adminEntity);
        return true;
    }

    @Override
    public String adminInfo(String token , String adminName) {
        //根据token从redis服务中查询用户身份信息
        String userInfo = redisTemplate.opsForValue().get(Constant.REDIS_ADMIN_LOGIN_KEY + token);

        AdminEntity adminEntity = this.getOne(new QueryWrapper<AdminEntity>().eq("adminName" ,adminName));

        if (StringUtils.isBlank(userInfo)){
            return null;
        }
        return adminEntity.toString();
    }
    //查询全部教师
    @Override
    public IPage<TeacherEntity> teacherInfo(Integer page , Integer size) {
        Page<TeacherEntity> teacherEntityPage = new Page<>(page, size);
        return teacherService.page(new Page<>(page, size) ,null);
    }
    //查询全部学生
    @Override
    public IPage<StudentEntity> studentInfo(Integer page , Integer size) {
        return studentService.page(new Page<>(page,size),null);
    }

    @Override
    //按条件查询
    public TeacherEntity teacherInfoByname(String teacherName ,Integer page , Integer size){
        QueryWrapper<TeacherEntity> wrapper = new QueryWrapper<>();
        wrapper.like("teacherName" , teacherName);
        return teacherService.getOne(wrapper);
    }

    @Override
    //管理员列表
    public IPage<AdminEntity> adminsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}