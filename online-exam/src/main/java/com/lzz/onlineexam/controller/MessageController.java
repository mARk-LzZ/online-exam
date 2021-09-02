package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lzz.onlineexam.common.exception.RRException;
import io.swagger.annotations.Api;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.MessageEntity;
import com.lzz.onlineexam.service.MessageService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 留言表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/message")
@Api(tags="留言控制器")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    @PreAuthorize("hasAuthority('message')")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", messageService.messagesInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
   // @RequiresPermissions("onlineexam:message:info")
    @PreAuthorize("hasAnyAuthority('message,student')")
    public R info(@PathVariable("id") Integer id){
		MessageEntity message = messageService.getById(id);

        return R.ok().put("message", message);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:message:save")
    @PreAuthorize("hasAnyAuthority('message,student')")
    public R save(@RequestBody MessageEntity message) throws RRException {
        if (message.getTitle() != null && message.getContent() != null){
            messageService.save(message);

            return R.ok();
        }
        throw new RRException("留言信息不完整"  , 1000);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:message:update")
    @PreAuthorize("hasAnyAuthority('message,student')")
    public R update(@RequestBody MessageEntity message){
		messageService.updateById(message);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:message:delete")
    @PreAuthorize("hasAnyAuthority('message,student')")
    public R delete(@RequestBody Integer[] ids){
		messageService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
