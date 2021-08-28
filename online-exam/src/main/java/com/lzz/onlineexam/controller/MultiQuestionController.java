package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzz.onlineexam.common.exception.RRException;
import com.lzz.onlineexam.entity.FillQuestionEntity;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Delete;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lzz.onlineexam.entity.MultiQuestionEntity;
import com.lzz.onlineexam.service.MultiQuestionService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 选择题题库表
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/multiquestion")
@Api(tags="选择题")
public class MultiQuestionController {
    @Autowired
    private MultiQuestionService multiQuestionService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", multiQuestionService.multiQuestionsInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{questionid}")
   // @RequiresPermissions("onlineexam:multiquestion:info")
    public R info(@PathVariable("questionid") Integer questionid){
		MultiQuestionEntity multiQuestion = multiQuestionService.getById(questionid);

        return R.ok().put("multiQuestion", multiQuestion);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //  @RequiresPermissions("onlineexam:fillquestion:save")
    public R save(@RequestBody MultiQuestionEntity multiQuestionEntity) throws RRException {
        if (multiQuestionEntity.getQuestion()!=null &&
                multiQuestionEntity.getAnswera()!= null &&
                multiQuestionEntity.getAnswerb()!= null &&
                multiQuestionEntity.getAnswerc()!= null &&
                multiQuestionEntity.getAnswerd()!= null &&
                multiQuestionEntity.getRightanswer()!= null &&
                multiQuestionEntity.getLevel() != null &&
                multiQuestionEntity.getSubject() != null &&
                multiQuestionEntity.getScore() !=null){
            multiQuestionService.save(multiQuestionEntity);
            List<MultiQuestionEntity> multiQuestionEntities=multiQuestionService.list(new QueryWrapper<MultiQuestionEntity>()
                    .eq("question", multiQuestionEntity.getQuestion())
                    .eq("rightAnswer", multiQuestionEntity.getRightanswer()));


            return R.ok().put("new multiQuestions" , multiQuestionEntities);
        }

        throw new RRException("题库信息不完整"  , 1000);
    }
    /**
     * 修改
     */
    @PostMapping("/update")
   // @RequiresPermissions("onlineexam:multiquestion:update")
    public R update(@RequestBody MultiQuestionEntity multiQuestion){
		multiQuestionService.updateById(multiQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:multiquestion:delete")
    public R delete(@RequestBody Integer[] questionids){
		multiQuestionService.removeByIds(Arrays.asList(questionids));

        return R.ok();
    }

}
