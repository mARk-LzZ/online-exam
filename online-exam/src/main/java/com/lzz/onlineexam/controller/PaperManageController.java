package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.entity.FillQuestionEntity;
import com.lzz.onlineexam.entity.JudgeQuestionEntity;
import com.lzz.onlineexam.entity.MultiQuestionEntity;
import com.lzz.onlineexam.service.FillQuestionService;
import com.lzz.onlineexam.service.JudgeQuestionService;
import com.lzz.onlineexam.service.MultiQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.PaperManageEntity;
import com.lzz.onlineexam.service.PaperManageService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 试卷管理表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/papermanage")
@Api(tags="试卷控制器")
public class PaperManageController {
    @Autowired
    private PaperManageService paperManageService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private JudgeQuestionService judgeQuestionService;

    @Autowired
    private MultiQuestionService multiQuestionService;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", paperManageService.papersInfo(page,size));

    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("出卷")
  //  @RequiresPermissions("onlineexam:papermanage:save")
    public R save(@RequestBody PaperManageEntity paperManage){
        if (paperManage.getQuestiontype() == null){
            throw new RRException("请指定题目类型" , 900);
        }else if (paperManage.getQuestionid() == null){
            throw new RRException("请指定题目id" , 901);
        }else if ((Double)paperManage.getPaperid() == 0.0){
            throw new RRException("请指定试卷id" , 902);
        }
		paperManageService.save(paperManage);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:papermanage:update")
    public R update(@RequestBody PaperManageEntity paperManage){
		paperManageService.updateById(paperManage);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:papermanage:delete")
    public R delete(@RequestBody Integer[] paperids){
		paperManageService.removeByIds(Arrays.asList(paperids));

        return R.ok();
    }

    /*
    * 查看试卷
    * */

    @ApiOperation("查看试卷")
    @PostMapping("/paperselect")
    @ApiImplicitParam(name="paperid" , value="试卷id")
    public R paperSelect(@ApiIgnore @RequestBody PaperManageEntity paperManageEntity) {
        Map<String, List<Object>> map=paperManageService.paperSelect(paperManageEntity.getPaperid());
        if (!map.isEmpty()){
            return R.ok().put("paper", map);
        }
        return R.error("试卷不存在");


    }


}
