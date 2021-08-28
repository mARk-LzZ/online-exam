package com.lzz.onlineexam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzz.onlineexam.common.utils.Constant;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.Query;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.controller.LoginController;
import com.lzz.onlineexam.dao.StudentDao;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.entity.ScoreEntity;
import com.lzz.onlineexam.entity.StudentEntity;
import com.lzz.onlineexam.service.LoginService;
import com.lzz.onlineexam.service.ScoreService;
import com.lzz.onlineexam.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Map;
import java.util.Random;


@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private LoginService loginService;
    @Autowired
    private ScoreService scoreService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StudentEntity> page=this.page(
                new Query<StudentEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }





    @Override
    public R register(StudentEntity studentEntity) {
        String vercode=studentEntity.getVercode();
        StudentEntity existsStudent=this.getOne(new QueryWrapper<StudentEntity>().eq("tel", studentEntity.getTel()));
        //号码注册过了
        if (existsStudent != null) {
            return R.error().put("message", 1);
        }
        //没有注册过号码 短信验证码超时
        if (LoginController.phonecodemap1.get(studentEntity.getTel()).isEmpty()) {
            return R.error().put("message", 2);
        }
        //短信验证码未超时 验证码验证错误
        if (!LoginController.phonecodemap1.get(studentEntity.getTel()).equals(vercode)) {
            return R.error().put("message", 3);
        }
        //短信验证码正确 进行注册 并存入登录表
        String pwd=new BCryptPasswordEncoder().encode(studentEntity.getPwd());
        StringBuilder cardId =new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            cardId.append(random.nextInt(10));
        }

        studentEntity.setCardid(cardId.toString());
        studentEntity.setPwd(pwd);
//        String studentId =studentEntity.getGrade() + majorService.majorNum(studentEntity.getMajor()) + studentEntity.getClazz() + ;
        this.getBaseMapper().insert(studentEntity);
        //存入登陆表
        LoginEntity loginEntity=new LoginEntity();
        loginEntity.setUserid(cardId.toString());
        loginEntity.setUsername(studentEntity.getStudentname());
        loginEntity.setTel(studentEntity.getTel());
        loginEntity.setPwd(pwd);
        loginEntity.setRole(2);
        loginService.save(loginEntity);

        return R.ok();

    }

    @Override
    public String studentInfo(String token , String studentName) {
        //根据token从redis服务中查询用户身份信息
        String userInfo = redisTemplate.opsForValue().get(Constant.REDIS_LOGIN_KEY+ token);
        if (StringUtils.isBlank(userInfo)){
            return null;
        }
        StudentEntity studentEntity = this.getOne(new QueryWrapper<StudentEntity>().eq("studentName" ,studentName));
        return studentEntity.toString();
    }

    @Override
    public Page<ScoreEntity> myExamRecord(String studentId , Integer page , Integer size) {
        QueryWrapper<ScoreEntity> wrapper = new QueryWrapper<ScoreEntity>().eq("studentId",studentId);
        return scoreService.page(new Page<>(page,size) , wrapper);
    }

    @Override
    public IPage<StudentEntity> studentsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}