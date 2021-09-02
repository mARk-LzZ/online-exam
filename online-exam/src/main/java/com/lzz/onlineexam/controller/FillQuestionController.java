package com.lzz.onlineexam.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import com.lzz.onlineexam.entity.FillQuestionEntity;
import com.lzz.onlineexam.service.FillQuestionService;
import com.lzz.onlineexam.common.utils.PageUtils;
import com.lzz.onlineexam.common.utils.R;



/**
 * 填空题题库
 *
 * @author lzz
 * @email 1399508400@qq.com
 * @date 2021-07-21 13:09:54
 */
@RestController
@RequestMapping("onlineexam/fillquestion")
@Api(tags={"填空题"})
public class FillQuestionController {
    @Autowired
    private FillQuestionService fillQuestionService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * 列表 并将数据同步到es中
     */
    @GetMapping("/list/{page}/{size}")
    // @RequiresPermissions("onlineexam:exammanage:list")
    @PreAuthorize("hasAnyAuthority('fillQuestion,student')")
    public R list(@PathVariable Integer page, @PathVariable Integer size ) {

        return R.ok().put("list", fillQuestionService.fillQuestionsInfo(page,size));

    }


    /**
     * 信息
     */
    @GetMapping("/info/{questionid}")
   // @RequiresPermissions("onlineexam:fillquestion:info")
    @PreAuthorize("hasAnyAuthority('fillQuestion,student')")
    public R info(@PathVariable("questionid") Integer questionid){
		FillQuestionEntity fillQuestion = fillQuestionService.getById(questionid);

        return R.ok().put("fillQuestion", fillQuestion);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
  //  @RequiresPermissions("onlineexam:fillquestion:save")
    @PreAuthorize("hasAuthority('fillQuestion')")
    public R save(@RequestBody FillQuestionEntity fillQuestion) throws RRException {
        if (fillQuestion.getQuestion()!=null &&
            fillQuestion.getAnswer()!= null &&
            fillQuestion.getLevel() != null &&
            fillQuestion.getSubject() != null &&
            fillQuestion.getScore() !=null){
            fillQuestionService.save(fillQuestion);
            List<FillQuestionEntity> fillQuestionEntities=fillQuestionService.list(new QueryWrapper<FillQuestionEntity>()
                    .eq("question", fillQuestion.getQuestion())
                    .eq("answer", fillQuestion.getAnswer()));


            return R.ok().put("new fileeQuestions" , fillQuestionEntities);
        }
        throw new RRException("试题信息不完整"  , 1000);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
   // @RequiresPermissions("onlineexam:fillquestion:update")
    @PreAuthorize("hasAuthority('fillQuestion')")
    public R update(@RequestBody FillQuestionEntity fillQuestion){
		fillQuestionService.updateById(fillQuestion);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
   // @RequiresPermissions("onlineexam:fillquestion:delete")
    @PreAuthorize("hasAuthority('fillQuestion')")
    public R delete(@RequestBody Integer[] questionids){
		fillQuestionService.removeByIds(Arrays.asList(questionids));

        return R.ok();
    }

}
