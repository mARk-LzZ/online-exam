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
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.ReplayEntity;
import com.lzz.onlineexam.service.ReplayService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 回复表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/replay")
@Api(tags="回复控制器")
public class ReplayController {
    @Autowired
    private ReplayService replayService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", replayService.replaysInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{replayid}")
   // @RequiresPermissions("onlineexam:replay:info")
    public R info(@PathVariable("replayid") Integer replayid){
		ReplayEntity replay = replayService.getById(replayid);

        return R.ok().put("replay", replay);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:replay:save")
    public R save(@RequestBody ReplayEntity replay) throws RRException {
        if (replay.getMessageid() != 0.0 && replay.getReplay() != null && replay.getReplayid()!= 0.0){
            replayService.save(replay);

            return R.ok();
        }
        throw new RRException("回复信息不完整"  , 1000);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:replay:update")
    public R update(@RequestBody ReplayEntity replay){
		replayService.updateById(replay);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:replay:delete")
    public R delete(@RequestBody Integer[] replayids){
		replayService.removeByIds(Arrays.asList(replayids));

        return R.ok();
    }

}
