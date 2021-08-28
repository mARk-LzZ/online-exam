package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import com.lzz.onlineexam.entity.LoginEntity;

import java.util.Map;

/**
 * 
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-25 16:30:03
 */
public interface LoginService extends IService<LoginEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //登录
    R login(LoginEntity loginEntity);


    IPage<LoginEntity> loginsInfo(Integer page, Integer size);


}

