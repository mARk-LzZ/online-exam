package com.lzz.onlineexam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.Constant;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.controller.LoginController;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.service.LoginService;
import com.lzz.onlineexam.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;

import com.lzz.onlineexam.dao.TeacherDao;
import com.lzz.onlineexam.entity.TeacherEntity;
import com.lzz.onlineexam.service.TeacherService;
import org.springframework.util.DigestUtils;


@Service("teacherService")
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, TeacherEntity> implements TeacherService {

    @Autowired
    private final RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();

    @Autowired
    private LoginService loginService;

    @Autowired
    private StudentService studentService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TeacherEntity> page = this.page(
                new Query<TeacherEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    //注册

    @Override
    public R register(TeacherEntity teacherEntity) {
        String vercode=teacherEntity.getVercode();
        TeacherEntity existsTeacher=this.getOne(new QueryWrapper<TeacherEntity>().eq("tel", teacherEntity.getTel()));
        //号码注册过了
        if (existsTeacher != null) {
            return R.error().put("message", 1);
        }
        //没有注册过号码 短信验证码超时
        if (LoginController.phonecodemap1.get(teacherEntity.getTel()).isEmpty()) {
            return R.error().put("message", 2);
        }
        //短信验证码未超时 验证码验证错误
        if (!LoginController.phonecodemap1.get(teacherEntity.getTel()).equals(vercode)) {
            return R.error().put("message", 3);
        }
        //短信验证码正确 进行注册 并存入登录表
        String pwd=new BCryptPasswordEncoder().encode(teacherEntity.getPwd());
        StringBuilder cardId =new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            cardId.append(random.nextInt(10));
        }

        teacherEntity.setCardid(cardId.toString());
        teacherEntity.setPwd(pwd);
//        String studentId =teacherEntity.getGrade() + majorService.majorNum(teacherEntity.getMajor()) + teacherEntity.getClazz() + ;
        this.getBaseMapper().insert(teacherEntity);
        //存入登陆表
        LoginEntity loginEntity=new LoginEntity();
        loginEntity.setUserid(cardId.toString());
        loginEntity.setUsername(teacherEntity.getTeachername());
        loginEntity.setTel(teacherEntity.getTel());
        loginEntity.setPwd(pwd);
        loginEntity.setRole(1);
        loginService.save(loginEntity);

        return R.ok();

    }
    @Override
    public String teacherInfo(String token , String teacherName) {
        //根据token从redis服务中查询用户身份信息
        String userInfo = redisTemplate.opsForValue().get(Constant.REDIS_LOGIN_KEY+ token);
        if (StringUtils.isBlank(userInfo)){
            return null;
        }
        TeacherEntity teacherEntity = this.getOne(new QueryWrapper<TeacherEntity>().eq("teacherName" ,teacherName));
        return teacherEntity.toString();
    }

    @Override
    public IPage<StudentEntity> studentsInfo(Integer page , Integer size){

        return studentService.page(new Page<>(page,size),null);
    }

    @Override
    public IPage<TeacherEntity> teachersInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}