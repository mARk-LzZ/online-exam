package com.lzz.onlineexam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.entity.MessageEntity;

import java.util.Map;

/**
 * 留言表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-20 23:58:21
 */
public interface MessageService extends IService<MessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    IPage<MessageEntity> messagesInfo(Integer page, Integer size);
}

