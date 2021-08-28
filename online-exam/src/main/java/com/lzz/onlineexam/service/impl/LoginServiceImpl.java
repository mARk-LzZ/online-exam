package com.lzz.onlineexam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzz.onlineexam.common.utils.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lzz.onlineexam.dao.LoginDao;
import com.lzz.onlineexam.entity.LoginEntity;
import com.lzz.onlineexam.service.LoginService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author lzz
 */
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginEntity> implements LoginService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LoginEntity> page = this.page(
                new Query<LoginEntity>().getPage(params),
                new QueryWrapper<LoginEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    //登录认证的接口实现
    public R login(LoginEntity loginEntity) {
        LoginEntity loginEntity1=this.getOne(new QueryWrapper<LoginEntity>()
                .eq("tel", loginEntity.getTel())
                .or()
                .eq("userid" , loginEntity.getUserid()));
        if (loginEntity1 != null) {
            boolean matches = new BCryptPasswordEncoder().matches(loginEntity.getPwd() , loginEntity1.getPwd());

            if (matches && loginEntity.getTel()!=null) {
                //如果验证通过表明登陆成功

                //生成一个token
                String token=DigestUtils.md5DigestAsHex(loginEntity1.getTel().getBytes());

                loginEntity1.setPwd(null);
                //把用户身份储存在redis中
                redisTemplate.opsForValue().set(Constant.REDIS_LOGIN_KEY + token, JSON.toJSONString(loginEntity1));
                //过期时间
                redisTemplate.expire(Constant.REDIS_LOGIN_KEY + token, 3, TimeUnit.HOURS);

                return R.ok(String.valueOf(loginEntity1.getRole()));
            } else if (matches && loginEntity.getUserid()!=null){
                //如果验证通过表明登陆成功

                //生成一个token
                String token=DigestUtils.md5DigestAsHex(loginEntity1.getUserid().getBytes());

                loginEntity1.setPwd(null);
                //把用户身份储存在redis中
                redisTemplate.opsForValue().set(Constant.REDIS_LOGIN_KEY + token, JSON.toJSONString(loginEntity1));
                //过期时间
                redisTemplate.expire(Constant.REDIS_LOGIN_KEY + token, 3, TimeUnit.HOURS);

                return R.ok(String.valueOf(loginEntity1.getRole()));
            }
            return R.error().put("message" ,1);
        }
        return R.error().put("message" ,2);
    }

    @Override
    public IPage<LoginEntity> loginsInfo(Integer page, Integer size) {
        return this.page(new Page<>(page,size));
    }


}